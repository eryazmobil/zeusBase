package eryaz.software.zeusBase.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class TransferPickingResponse(

    @SerializedName("transferRequestDetailList")
    val transferRequestDetailList: List<TransferRequestDetailResponse>,
    @SerializedName("pickingSuggestionList")
    val pickingSuggestionList: List<PickingSuggestionResponse>
)