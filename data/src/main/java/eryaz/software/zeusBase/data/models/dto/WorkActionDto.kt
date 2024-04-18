package eryaz.software.zeusBase.data.models.dto

import eryaz.software.zeusBase.data.models.remote.response.WorkActivityTypeResponse

data class WorkActionDto(
    val workActionId: Int,
    val workActivity: WorkActivityDto,
    val workActionType: WorkActivityTypeResponse,
    val processUser: CurrentUserDto,
    val isFinished: Boolean
)