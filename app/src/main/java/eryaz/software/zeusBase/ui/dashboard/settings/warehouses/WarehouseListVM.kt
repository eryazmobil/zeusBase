package eryaz.software.zeusBase.ui.dashboard.settings.warehouses

import eryaz.software.zeusBase.data.models.dto.WarehouseDto
import eryaz.software.zeusBase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WarehouseListVM(
    val warehouseListDto :List<WarehouseDto>
) : BaseViewModel() {

    private val _warehouseList = MutableStateFlow(warehouseListDto)
    val warehouseList = _warehouseList.asStateFlow()
}