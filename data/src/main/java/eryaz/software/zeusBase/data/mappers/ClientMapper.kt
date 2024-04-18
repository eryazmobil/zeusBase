package eryaz.software.zeusBase.data.mappers

import eryaz.software.zeusBase.data.models.dto.ClientDto
import eryaz.software.zeusBase.data.models.remote.response.ClientSmallResponse

fun ClientSmallResponse.toDto() = ClientDto(
    id = id,
    code = code,
    name = name ?: "Hatalı işlem"
)