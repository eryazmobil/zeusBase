package eryaz.software.zeusBase.ui.dashboard.inbound.crossdockList

import eryaz.software.zeusBase.data.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.CrossDockDto
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.WarningDialogDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.OrderRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CrossDockListVM(private val repo: OrderRepo) : BaseViewModel() {

    private val _crossDockList = MutableStateFlow(listOf<CrossDockDto>())
    val crossDockList = _crossDockList.asStateFlow()

    init {
        fetchCrossDockList()
    }

    fun fetchCrossDockList() {
        executeInBackground(_uiState) {
            repo.fetchCrossDockList(
                companyId = SessionManager.companyId,
                warehouseId = SessionManager.warehouseId
            ).onSuccess {
                if (it.isEmpty()) {
                    showWarning(
                        WarningDialogDto(
                            title = stringProvider.invoke(R.string.warning),
                            message = stringProvider.invoke(R.string.crossdock_empty_list)
                        )
                    )
                }
                _crossDockList.emit(it)
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