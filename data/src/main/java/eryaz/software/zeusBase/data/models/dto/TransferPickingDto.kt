package eryaz.software.zeusBase.data.models.dto

data class TransferPickingDto(

    val transferRequestDetailList: List<TransferRequestDetailDto>,
    val pickingSuggestionList: List<PickingSuggestionDto>
)