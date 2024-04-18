package eryaz.software.zeusBase.ui.dashboard.outbound.datControl

import androidx.lifecycle.MutableLiveData
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.onError
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.WarningDialogDto
import eryaz.software.zeusBase.data.models.dto.WorkActivityDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.WorkActivityRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class ControlPointTransferListVM(private val repo: WorkActivityRepo) : BaseViewModel() {

    val search = MutableLiveData("")

    private val _workActivityList = MutableStateFlow(listOf<WorkActivityDto>())
    val workActivityList = _workActivityList.asSharedFlow()

    fun getTransferWorkActivityList() {
        executeInBackground(_uiState) {
            repo.getTransferWarehouseWorkActivityListForPdaControl(
                warehouseId = SessionManager.warehouseId,
                companyId = SessionManager.companyId
            ).onSuccess {
                if (it.isNotEmpty()) {
                    _workActivityList.emit(it)
                } else {
                    showWarning(
                        WarningDialogDto(
                            title = stringProvider.invoke(R.string.not_found_work_activity),
                            message = stringProvider.invoke(R.string.list_is_empty)
                        )
                    )
                }
            }.onError { message, _ ->
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error,
                        message = message
                    )
                )
            }
        }
    }
}