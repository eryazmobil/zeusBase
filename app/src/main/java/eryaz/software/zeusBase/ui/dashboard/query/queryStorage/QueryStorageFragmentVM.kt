package eryaz.software.zeusBase.ui.dashboard.query.queryStorage

import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.BarcodeDto
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.ProductDto
import eryaz.software.zeusBase.data.models.dto.ProductShelfQuantityDto
import eryaz.software.zeusBase.data.models.dto.ProductStorageQuantityDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.WorkActivityRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueryStorageFragmentVM(
    private val repo: WorkActivityRepo
) : BaseViewModel() {
    val searchProduct = MutableStateFlow("")

    private val _productDetail = MutableStateFlow<ProductDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _showProductDetail = MutableStateFlow(false)
    val showProductDetail = _showProductDetail.asStateFlow()

    private val _storageList = MutableStateFlow(listOf<ProductStorageQuantityDto>())
    val storageList = _storageList.asStateFlow()

    private val _shelfList = MutableStateFlow(listOf<ProductShelfQuantityDto>())
    val shelfList = _shelfList.asStateFlow()

    fun getBarcodeByCode() = executeInBackground(showProgressDialog = true) {
        repo.getBarcodeByCode(
            searchProduct.value.trim(),
            SessionManager.companyId
        ).onSuccess {
            _productDetail.emit(it.product)
            getProductStorageQuantityList(it.product.id)
            getProductShelfQuantityList(it.product.id)
        }.onError { message, _ ->
            _showProductDetail.emit(false)
            showError(
                ErrorDialogDto(
                    title = stringProvider.invoke(R.string.error),
                    message = message
                )
            )
        }
    }

    private fun getProductStorageQuantityList(id: Int) =
        executeInBackground(showProgressDialog = true) {

            repo.getProductStorageQuantityList(
                productId = id,
                warehouseId = SessionManager.warehouseId,
                storageId = 0,
                companyId = SessionManager.companyId
            ).onSuccess {
                _storageList.emit(it)
            }
        }

    private fun getProductShelfQuantityList(id: Int) =
        executeInBackground(showProgressDialog = true) {

            repo.getProductShelfQuantityList(
                productId = id,
                warehouseId = SessionManager.warehouseId,
                storageId = 0,
                companyId = SessionManager.companyId,
                shelfId = 0
            ).onSuccess {
                _shelfList.emit(it)
                _showProductDetail.emit(true)
            }
        }

    fun setExitStorage(it: ProductDto) {
        getProductStorageQuantityList(it.id)
        getProductShelfQuantityList(it.id)
        _productDetail.value = it
    }
}