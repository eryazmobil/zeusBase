package eryaz.software.zeusBase.ui.dashboard.outbound.orderPicking.orderPickingDetail.dialog

import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.ProductShelfQuantityDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.WorkActivityRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShelfListDialogVM(
    private val repo: WorkActivityRepo,
    private val productId: Int
) : BaseViewModel() {

    private val _shelfList = MutableStateFlow(listOf<ProductShelfQuantityDto>())
    val shelfList = _shelfList.asStateFlow()

    init {
        getOrderListDetail()
    }

    fun getOrderListDetail() {
        executeInBackground(showProgressDialog = true) {
            repo.getProductShelfQuantityList(
                productId = productId,
                warehouseId = SessionManager.warehouseId,
                companyId = SessionManager.companyId,
                storageId = 0,
                shelfId = 0)
                .onSuccess {
                    _shelfList.emit(it)
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

}