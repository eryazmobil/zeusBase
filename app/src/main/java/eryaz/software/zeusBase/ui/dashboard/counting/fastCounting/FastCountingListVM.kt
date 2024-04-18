package eryaz.software.zeusBase.ui.dashboard.counting.fastCounting

import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.StockTakingHeaderDto
import eryaz.software.zeusBase.data.models.dto.WarningDialogDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.CountingRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FastCountingListVM(private val repo: CountingRepo) : BaseViewModel() {

    private val _countingWorkActivityList = MutableStateFlow(listOf<StockTakingHeaderDto>())
    val countingWorkActivityList = _countingWorkActivityList.asStateFlow()

    val assignedUser = MutableSharedFlow<Boolean>()

    var stockTackingHeaderId: Int = 0
    var stockTackingDetailId: Int = 0
    var countingWorkActivityId: Int = 0

    fun fetchCountingWorkActivityList() {

        executeInBackground(_uiState) {
            repo.getCountingWorkActivityList(
                companyId = SessionManager.companyId,
                warehouseId = SessionManager.warehouseId
            ).onSuccess {
                if (it.isEmpty()) {
                    _countingWorkActivityList.emit(emptyList())
                    showWarning(
                        WarningDialogDto(
                            title = stringProvider.invoke(R.string.not_found_work_activity),
                            message = stringProvider.invoke(R.string.list_is_empty)
                        )
                    )
                } else {
                    _countingWorkActivityList.emit(it)
                }
            }.onError { _, _ ->
                _countingWorkActivityList.emit(emptyList())
            }
        }
    }

    fun getAllAssignedToUser(stHeaderId: Int) {
        executeInBackground(_uiState) {
            repo.getAllAssignedToUser(
                stActionProcessId = stHeaderId,
                userId = SessionManager.userId
            ).onSuccess {
                if (it.isEmpty()) {
                    showError(
                        ErrorDialogDto(
                            titleRes = R.string.error,
                            messageRes = R.string.no_assigned_work
                        )
                    )
                }else {
                    stockTackingDetailId = it[0].id
                    assignedUser.emit(true)
                }
            }
        }
    }

    companion object {
        const val FAST_COUNTING_WAREHOUSE = 16
    }
}
