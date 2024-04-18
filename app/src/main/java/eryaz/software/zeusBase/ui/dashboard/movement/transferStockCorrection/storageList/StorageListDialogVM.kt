package eryaz.software.zeusBase.ui.dashboard.movement.transferStockCorrection.storageList

import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.models.dto.StorageDto
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.UserRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StorageListDialogVM(
    private val userRepo: UserRepo,
) : BaseViewModel() {

    private val _storageList = MutableStateFlow(listOf<StorageDto>())
    val storageList = _storageList.asStateFlow()

    init {
        getStorageListByWarehouse()
    }

    fun getStorageListByWarehouse() = executeInBackground(_uiState) {
        userRepo.getStorageListByWarehouse(
            warehouseId = SessionManager.warehouseId
        ).onSuccess {
            _storageList.emit(it)
        }
    }
}