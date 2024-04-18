package eryaz.software.zeusBase.ui.dashboard.movement.routeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.RouteDto
import eryaz.software.zeusBase.data.models.dto.WarningDialogDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.OrderRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel

class RouteListVM(private val repo: OrderRepo) : BaseViewModel() {

    private val _routeList = MutableLiveData<List<RouteDto?>>(emptyList())
    val routeList: LiveData<List<RouteDto?>> = _routeList

    val search = MutableLiveData("")

    fun searchList() = search.switchMap { query ->
        MutableLiveData<List<RouteDto?>>().apply {
            value = filterData(query)
        }
    }

    private fun filterData(query: String): List<RouteDto?> {
        val dataList = _routeList.value.orEmpty()

        val filteredList = dataList.filter { data ->
            data?.code?.contains(query, ignoreCase = true) ?: true
        }
        return filteredList
    }

    fun fetchRouteList() {
        executeInBackground(_uiState) {
            repo.getRouteList(
                SessionManager.warehouseId
            ).onSuccess {
                if (it.isEmpty()) {
                    _routeList.value = emptyList()
                    showWarning(
                        WarningDialogDto(
                            title = stringProvider.invoke(R.string.route_not_founded),
                            message = stringProvider.invoke(R.string.list_is_empty)
                        )
                    )
                }
                _routeList.value = it

            }.onError { _, _ ->
                _routeList.value = emptyList()

            }
        }
    }
}