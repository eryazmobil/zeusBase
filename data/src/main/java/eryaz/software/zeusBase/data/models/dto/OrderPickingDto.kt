package eryaz.software.zeusBase.data.models.dto

data class OrderPickingDto(

    val orderDetailList: List<OrderDetailDto>,
    val pickingSuggestionList: List<PickingSuggestionDto>,
)