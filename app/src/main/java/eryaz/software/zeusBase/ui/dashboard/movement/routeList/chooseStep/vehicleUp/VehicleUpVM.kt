package eryaz.software.zeusBase.ui.dashboard.movement.routeList.chooseStep.vehicleUp

import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.VehiclePackageDto
import eryaz.software.zeusBase.data.repositories.OrderRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VehicleUpVM(
    val repo: OrderRepo,
    val vehicleID: Int,
    val routeID: Int
) : BaseViewModel() {

    private val _packageList = MutableStateFlow(listOf<VehiclePackageDto>())
    val packageList = _packageList.asStateFlow()

    val packageCode = MutableStateFlow("")
    val deliveredNameTxt = MutableStateFlow("")
    val vehicleDownSuccess = MutableStateFlow(false)

    init {
        getOrderHeaderRouteList()
    }

    fun createOrderHeaderRoute() {
        executeInBackground {
            repo.updateOrderHeaderRoute(
                code = packageCode.value,
                shippingRouteId = routeID,
                deliveredPerson = deliveredNameTxt.value
            ).onSuccess {
                vehicleDownSuccess.emit(true)
                getOrderHeaderRouteList()
            }.onError { message, _ ->
                ErrorDialogDto(
                    title = stringProvider.invoke(R.string.error),
                    message = message
                )
            }.also {
                packageCode.emit("")
                deliveredNameTxt.emit("")
            }
        }
    }

    fun getOrderHeaderRouteList() {
        executeInBackground(showProgressDialog = true) {
            repo.getOrderHeaderRouteList(
                shippingRouteId = routeID
            ).onSuccess {
                if (it.isNotEmpty()) {
                    _packageList.emit(it)
                }
            }
        }
    }
}