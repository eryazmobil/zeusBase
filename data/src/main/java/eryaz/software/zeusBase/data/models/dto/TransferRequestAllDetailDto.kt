package eryaz.software.zeusBase.data.models.dto

data class TransferRequestAllDetailDto(
    val transferRequestHeader: TransferRequestHeaderDto,
    val quantityShippedAll: Int,
    val quantityPickedAll: Int,
    val quantityAll: Int,
    val transferRequestDetailPdaDto: List<TransferRequestDetailDto>
)