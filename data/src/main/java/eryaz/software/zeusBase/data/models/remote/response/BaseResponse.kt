package eryaz.software.zeusBase.data.models.remote.response

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("success")
    val success: Boolean = false
)
