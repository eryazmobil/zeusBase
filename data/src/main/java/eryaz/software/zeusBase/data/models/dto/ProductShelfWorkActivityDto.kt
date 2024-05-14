package eryaz.software.zeusBase.data.models.dto

import eryaz.software.zeusBase.data.persistence.SessionManager

data class ProductShelfWorkActivityDto(
    val companyId: Int? =SessionManager.companyId,
    val warehouseId: Int? = SessionManager.warehouseId,
    val storageId: Int? = 1,
    val assignedUserId: Int? = SessionManager.userId,
    val productShelfIdList: List<Int?>? =null,
    val workActivityId: Int? = null
)
