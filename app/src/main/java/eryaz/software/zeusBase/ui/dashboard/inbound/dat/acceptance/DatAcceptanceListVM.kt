package eryaz.software.zeusBase.ui.dashboard.inbound.dat.acceptance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.enums.ActionType
import eryaz.software.zeusBase.data.models.dto.WarningDialogDto
import eryaz.software.zeusBase.data.models.dto.WorkActionDto
import eryaz.software.zeusBase.data.models.dto.WorkActivityDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.persistence.TemporaryCashManager
import eryaz.software.zeusBase.data.repositories.WorkActivityRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import eryaz.software.zeusBase.util.extensions.orZero
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DatAcceptanceListVM(private val repo: WorkActivityRepo) : BaseViewModel() {

    private val _acceptanceList = MutableLiveData<List<WorkActivityDto?>>(emptyList())
    val acceptanceList: LiveData<List<WorkActivityDto?>> = _acceptanceList

    val search = MutableLiveData("")

    private val _workActionDto = MutableSharedFlow<WorkActionDto>()
    val workActionDto = _workActionDto.asSharedFlow()

    fun searchList() = search.switchMap { query ->
        MutableLiveData<List<WorkActivityDto?>>().apply {
            value = filterData(query)
        }
    }

    // Filtreleme koşulları
    private fun filterData(query: String): List<WorkActivityDto?> {
        val dataList = _acceptanceList.value.orEmpty()

        val filteredList = dataList.filter { data ->
            data?.client?.name?.contains(query, ignoreCase = true) ?: true
        }
        return filteredList
    }

    fun getWaybillWorkActivityList() {
        executeInBackground(_uiState) {
            repo.getTransferRequestReceivingWorkActivityList(
                SessionManager.companyId,
                SessionManager.warehouseId
            ).onSuccess {
                if (it.isEmpty()) {
                    _acceptanceList.value = emptyList()
                    showWarning(
                        WarningDialogDto(
                            title = stringProvider.invoke(R.string.not_found_work_activity),
                            message = stringProvider.invoke(R.string.list_is_empty)
                        )
                    )
                }
                _acceptanceList.value = it

            }.onError { _, _ ->
                _acceptanceList.value = emptyList()

            }
        }
    }

    fun getWorkActionForPda() {
        executeInBackground(
            _uiState,
            showErrorDialog = false,
            checkErrorState = false
        ) {
            repo.getWorkActionForPda(
                userId = SessionManager.userId,
                workActivityId = TemporaryCashManager.getInstance().workActivity?.workActivityId.orZero(),
                actionTypeId =  TemporaryCashManager.getInstance().workActionTypeList?.find { model -> model.code == "Control" }?.id.orZero()
            ).onSuccess {
                _workActionDto.emit(it)
                TemporaryCashManager.getInstance().workAction = it
            }.onError { _, _ ->
                createWorkAction()
            }
        }
    }

    private fun createWorkAction() {
        executeInBackground {
            repo.createWorkAction(
                activityId = TemporaryCashManager.getInstance().workActivity?.workActivityId ?: 0,
                actionTypeCode = ActionType.CONTROL.type
            ).onSuccess {
                TemporaryCashManager.getInstance().workAction = it
                _workActionDto.emit(it)
            }
        }
    }
}