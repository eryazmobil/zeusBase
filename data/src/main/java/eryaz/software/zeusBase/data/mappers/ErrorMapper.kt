package eryaz.software.zeusBase.data.mappers

import eryaz.software.zeusBase.data.models.dto.ErrorDto
import eryaz.software.zeusBase.data.models.remote.response.ErrorModel

fun ErrorModel.toDto() = ErrorDto(
    code = code,
    message = message
)