package eryaz.software.zeusBase.data.mappers

import eryaz.software.zeusBase.data.models.dto.CurrentUserDto
import eryaz.software.zeusBase.data.models.dto.WorkActionDto
import eryaz.software.zeusBase.data.models.remote.response.CurrentUserResponse
import eryaz.software.zeusBase.data.models.remote.response.WorkActionResponse

fun CurrentUserResponse.toDto() = CurrentUserDto(
    userId = userId,
    fullName = "$name $surname",
    email = emailAddress,
    username = userName,
    companyId = companyId,
    warehouseId = warehouseId
)

fun WorkActionResponse.toDto() = WorkActionDto(
    workActionId = id,
    workActivity = workActivity.toDto(),
    workActionType = workActionType,
    processUser = processUser,
    isFinished = isFinished
)

