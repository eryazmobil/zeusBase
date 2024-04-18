package eryaz.software.zeusBase.data.mappers

import eryaz.software.zeusBase.data.models.dto.WaybillListDetailDto
import eryaz.software.zeusBase.data.models.remote.response.WaybillListDetailResponse

fun WaybillListDetailResponse.toDto() = WaybillListDetailDto(
    product = product.toDto(),
    quantity = quantity,
    quantityOrder = quantityOrder,
    quantityPlaced = quantityPlaced,
    quantityControlled = quantityControlled.toString(),
    id = id
)