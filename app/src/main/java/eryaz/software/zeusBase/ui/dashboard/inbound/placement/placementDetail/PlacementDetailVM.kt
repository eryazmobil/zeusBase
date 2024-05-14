package eryaz.software.zeusBase.ui.dashboard.inbound.placement.placementDetail

import androidx.lifecycle.viewModelScope
import eryaz.software.zeusBase.data.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.BarcodeDto
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.ProductDto
import eryaz.software.zeusBase.data.models.dto.ShelfDto
import eryaz.software.zeusBase.data.models.dto.WaybillListDetailDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.persistence.TemporaryCashManager
import eryaz.software.zeusBase.data.repositories.WorkActivityRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlacementDetailVM(
    private val repo: WorkActivityRepo
) : BaseViewModel() {

    val suggestionShelfList = MutableStateFlow<List<ShelfDto>>(emptyList())
    val waybillDetailList = MutableStateFlow<List<WaybillListDetailDto>>(emptyList())

    val searchProduct = MutableStateFlow("")
    val searchShelf = MutableStateFlow("")
    val productMultiplier = MutableStateFlow("")
    val enteredAmount = MutableStateFlow("")
    var errorMessage = MutableStateFlow("")
    val placementProductValue = MutableStateFlow("")
    val resultAmountTxt = MutableStateFlow("")
    val checkProductInPlacementList = MutableStateFlow(false)

    private var productID: Int = 0
    private var shelfID: Int = 0
    private var productCollected: Int = 0
    private var quantityPlaced: Int = 0

    private val _orderDate = MutableStateFlow("")
    val orderDate = _orderDate.asStateFlow()

    private val _clientName = MutableStateFlow("")
    val clientName = _clientName.asStateFlow()

    private val _productCode = MutableStateFlow("")
    val productCode = _productCode.asStateFlow()

    private val _productDetail = MutableStateFlow<ProductDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _actionIsFinished = MutableStateFlow(false)
    val actionIsFinished = _actionIsFinished.asStateFlow()

    private val _controlSuccess = MutableSharedFlow<Boolean>()
    val controlSuccess = _controlSuccess.asSharedFlow()

    private val _hasCrossDock = MutableSharedFlow<Boolean>()
    val hasCrossDock = _hasCrossDock.asSharedFlow()

    private val _showProductDetailView = MutableStateFlow(false)
    val showProductDetailView = _showProductDetailView.asStateFlow()

    init {
        TemporaryCashManager.getInstance().workActivity?.let {
            viewModelScope.launch {
                _clientName.emit(it.client!!.name)
                _orderDate.emit(it.creationTime)
                _productCode.emit(it.workActivityCode)
            }
        }
        getWaybillListDetail()

    }

    private fun getWaybillListDetail() {
        executeInBackground(_uiState) {
            val workActivityID =
                TemporaryCashManager.getInstance().workActivity?.workActivityId ?: 0
            repo.getWaybillListDetail(
                workActivityId = workActivityID
            ).onSuccess {
                waybillDetailList.emit(it)
                checkIfAllFinished()
            }.onError { message, _ ->
                showError(
                    ErrorDialogDto(
                        title = stringProvider.invoke(R.string.error),
                        message = message
                    )
                )
            }
        }
    }

    fun isQuantityValid(): Boolean {
        return enteredAmount.value.isEmpty() || enteredAmount.value == "0"
    }

    fun getBarcodeByCode() {
        executeInBackground(showErrorDialog = false) {
            repo.getBarcodeByCode(
                searchProduct.value.trim(), SessionManager.companyId
            ).onSuccess {
                _productDetail.emit(it.product)
                productID = it.product.id
                productMultiplier.value = it.quantity.toString()
                isProductValid()
            }.onError { _, _ ->
                showError(
                    ErrorDialogDto(
                        title = stringProvider.invoke(R.string.error),
                        message = stringProvider.invoke(R.string.msg_no_barcode)
                    )
                )
            }
        }
    }

    private fun isProductValid() {
        val currentList = waybillDetailList.value
        currentList.any {
            it.product.id == productID
        }.let { hasProduct ->
            when (hasProduct) {
                true -> {
                    getWaybillDetailQuantityWaitingForPlacement()
                }

                false -> {
                    showError(
                        ErrorDialogDto(
                            title = stringProvider.invoke(R.string.error),
                            message = stringProvider.invoke(R.string.msg_not_in_placement_list)
                        )
                    )
                }
            }
        }
    }

    private fun checkIfAllFinished(): Boolean {
        var hasDifferentValues = false

        waybillDetailList.value.forEach { waybillDetail ->
                productCollected = waybillDetail.quantityControlled.toInt()
                quantityPlaced = waybillDetail.quantityPlaced
                if (quantityPlaced != productCollected) {
                    hasDifferentValues = true
                }
        }
        val allFinished = !hasDifferentValues

        viewModelScope.launch {
            checkProductInPlacementList.emit(allFinished)
        }

        return allFinished
    }

    fun getShelfByCode() {
        executeInBackground(
            showErrorDialog = true,
            showProgressDialog = true
        ) {
            repo.getShelfByCode(
                code = searchShelf.value.trim(),
                warehouseId = SessionManager.warehouseId,
                storageId = 0
            ).onSuccess {
                shelfID = it.shelfId
                resultAmountTxt.emit(
                    enteredAmount.value +
                            stringProvider.invoke(
                                R.string.verify_qty_placement
                            )
                )
            }.onError { _, _ ->
                showError(
                    ErrorDialogDto(
                        title = stringProvider.invoke(R.string.error),
                        message = stringProvider.invoke(R.string.msg_shelf_not_found)
                    )
                )
            }
        }
    }

    private fun checkCrossDock() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground {
                repo.getCrossDockRequestForPlacing(
                    productId = productID,
                    companyId = SessionManager.companyId,
                    warehouseId = SessionManager.warehouseId
                ).onSuccess {
                    _hasCrossDock.emit(true)
                }.onError { _, _ ->
                    getShelfListForPlacement()
                }
            }
        }
    }

    private fun getShelfListForPlacement() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground(showProgressDialog = true) {
                repo.getShelfListForPlacement(
                    productId = productID,
                    false,
                    it.workActionId
                ).onSuccess {
                    suggestionShelfList.emit(
                        it.takeIf {
                            it.isNotEmpty()
                        } ?: listOf(
                            ShelfDto(
                                shelfId = 0,
                                shelfAddress = stringProvider.invoke(R.string.msg_no_shelf_suggestions),
                                quantity = ""
                            )
                        )
                    )
                }
            }
        }
    }

    fun updateWaybillControlAddQuantity() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground(showErrorDialog = false) {
                repo.updateWaybillPlacementAddQuantity(
                    actionId = it.workActionId,
                    productId = productID,
                    quantity = enteredAmount.value.toInt(),
                    shelfId = shelfID,
                    containerId = 0,
                    crossDockId = 0
                ).onSuccess {
                    _controlSuccess.emit(it)
                    _showProductDetailView.emit(false)
                    getWaybillListDetail()
                }.onError { message, _ ->
                    if (message.equals("QTY-EXC")) {
                        errorMessage.emit(stringProvider.invoke(eryaz.software.zeusBase.R.string.can_not_placement))
                    } else {
                        errorMessage.emit(message.orEmpty())
                    }

                    showError(
                        ErrorDialogDto(
                            title = stringProvider.invoke(R.string.error),
                            message = errorMessage.value
                        )
                    )
                }
            }
        }
    }

    private fun getWaybillDetailQuantityWaitingForPlacement() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground(showProgressDialog = true) {
                repo.getWaybillDetailQuantityWaitingForPlacement(
                    actionId = it.workActionId, productId = productID
                ).onSuccess {
                    when (it) {
                        0 -> {
                            searchProduct.value = ""
                            showError(
                                ErrorDialogDto(
                                    title = stringProvider.invoke(R.string.error),
                                    message = stringProvider.invoke(R.string.controlled_notcompleted)
                                )
                            )
                        }

                        else -> {
                            viewModelScope.launch {
                                _showProductDetailView.emit(true)
                            }
                            val multipleSign = "x ${productMultiplier.value} / $it"
                            placementProductValue.emit(multipleSign)
                            checkCrossDock()
                        }
                    }
                }.onError { message, _ ->
                    showError(
                        ErrorDialogDto(
                            title = stringProvider.invoke(R.string.error), message = message
                        )
                    )
                }
            }
        }
    }

    fun finishWorkAction() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground {
                repo.finishWorkAction(
                    actionId = it.workActionId
                ).onSuccess {
                    _actionIsFinished.emit(true)
                }.onError { message, statusEnum ->
                    showError(
                        ErrorDialogDto(
                            title = stringProvider.invoke(R.string.error), message = message
                        )
                    )
                }
            }
        }
    }

    fun setEnteredProduct(dto: ProductDto) {
        productID = dto.id
        viewModelScope.launch {
            _productDetail.emit(dto)
            _showProductDetailView.emit(true)
            isProductValid()
        }
    }
}