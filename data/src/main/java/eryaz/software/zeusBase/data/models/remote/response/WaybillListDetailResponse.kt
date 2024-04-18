package eryaz.software.zeusBase.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class WaybillListDetailResponse(

    @SerializedName("product")
    val product: ProductResponse,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("quantityOrder")
    val quantityOrder: Int,

    @SerializedName("quantityPlaced")
    val quantityPlaced: Int,

    @SerializedName("quantityControlled")
    val quantityControlled: Int,

    @SerializedName("id")
    val id: Int
)