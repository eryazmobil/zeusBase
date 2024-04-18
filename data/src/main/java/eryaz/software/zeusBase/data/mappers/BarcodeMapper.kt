package eryaz.software.zeusBase.data.mappers

import eryaz.software.zeusBase.data.models.dto.BarcodeDto
import eryaz.software.zeusBase.data.models.remote.response.BarcodeResponse

fun BarcodeResponse.toDto() = BarcodeDto(
    id = id,
    product = product.toDto(),
    code = code,
    quantity = quantity
)