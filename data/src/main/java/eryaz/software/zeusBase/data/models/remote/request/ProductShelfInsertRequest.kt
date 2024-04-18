package eryaz.software.zeusBase.data.models.remote.request

import com.google.gson.annotations.SerializedName

data class ProductShelfInsertRequest(

    @SerializedName("productId")
    val productId: Int,

    @SerializedName("shelfId")
    val shelfId: Int,

    @SerializedName("shelfTypeId")
    val shelfTypeId: Int,

    @SerializedName("safetyPercent")
    val safetyPercent: Int,

    @SerializedName("maxQuantity")
    val maxQuantity: Int
    )