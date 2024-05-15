package eryaz.software.zeusBase.ui.dashboard.movement.supply.supplyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.enums.ActionType
import eryaz.software.zeusBase.data.enums.ActivityType
import eryaz.software.zeusBase.data.models.dto.WarningDialogDto
import eryaz.software.zeusBase.data.models.dto.WorkActivityDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.persistence.TemporaryCashManager
import eryaz.software.zeusBase.data.repositories.WorkActivityRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import eryaz.software.zeusBase.util.extensions.orZero
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SupplyListVM(
    private val workActivityRepo: WorkActivityRepo
) : BaseViewModel() {

    private val _navigateToDetail = MutableSharedFlow<Boolean>()
    val navigateToDetail = _navigateToDetail.asSharedFlow()

    private val _supplyWorkActivityList = MutableLiveData<List<WorkActivityDto?>>(emptyList())
    val supplyWorkActivityList: LiveData<List<WorkActivityDto?>> = _supplyWorkActivityList

    val search = MutableLiveData("")


    fun searchList() = search.switchMap { query ->
        MutableLiveData<List<WorkActivityDto?>>().apply {
            value = filterData(query)
        }
    }

    private fun filterData(query: String): List<WorkActivityDto?> {
        val dataList = _supplyWorkActivityList.value.orEmpty()

        val filteredList = dataList.filter { data ->
            data?.workActivityCode?.contains(query, ignoreCase = true) ?: true
        }
        return filteredList
    }

    fun getActiveWorkAction() =
        executeInBackground(
            showErrorDialog = false,
            showProgressDialog = false
        ) {
            workActivityRepo.getWorkActionActive(
                companyId = SessionManager.companyId,
                warehouseId = SessionManager.warehouseId,
                workActivityType = ActivityType.REPLENISHMENT.type,
                workActionType = ActionType.REPLENISH.type
            ).onSuccess {
                _navigateToDetail.emit(true)
                TemporaryCashManager.getInstance().workAction = it
                TemporaryCashManager.getInstance().workActivity = it.workActivity
            }.onError { _, _ ->
                _navigateToDetail.emit(false)
                getUnfinishedReplenishmentWorkActivityListForPda()
            }
        }

    private fun getUnfinishedReplenishmentWorkActivityListForPda() {
        executeInBackground(_uiState) {
            workActivityRepo.getUnfinishedReplenishmentWorkActivityListForPda()
                .onSuccess {
                    if (it.isNotEmpty()) {
                        _supplyWorkActivityList.value = it
                    } else {
                        _supplyWorkActivityList.value = emptyList()
                        showWarning(
                            WarningDialogDto(
                                title = stringProvider.invoke(R.string.not_found_work_activity),
                                message = stringProvider.invoke(R.string.list_is_empty)
                            )
                        )
                    }
                }
        }
    }

    fun getWorkActionForPda() {
        executeInBackground(
            showProgressDialog = true,
            hasNextRequest = true,
            showErrorDialog = false
        ) {
            workActivityRepo.getWorkActionForPda(
                userId = SessionManager.userId,
                workActivityId = TemporaryCashManager.getInstance().workActivity?.workActivityId.orZero(),
                actionTypeId = TemporaryCashManager.getInstance().workActionTypeList?.find { model -> model.code == ActivityType.REPLENISHMENT.type }?.id.orZero()
            ).onSuccess {
                TemporaryCashManager.getInstance().workAction = it
                _navigateToDetail.emit(true)
            }.onError { _, _ ->
                createWorkAction()
            }
        }
    }

    private fun createWorkAction() {
        executeInBackground(showProgressDialog = true) {
            workActivityRepo.createWorkAction(
                activityId = TemporaryCashManager.getInstance().workActivity?.workActivityId ?: 0,
                actionTypeCode = ActionType.REPLENISH.type
            ).onSuccess {
                TemporaryCashManager.getInstance().workAction = it
                _navigateToDetail.emit(true)
            }
        }
    }

}