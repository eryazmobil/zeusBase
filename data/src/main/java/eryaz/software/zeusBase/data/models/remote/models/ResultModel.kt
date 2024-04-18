package eryaz.software.zeusBase.data.models.remote.models

import com.google.gson.annotations.SerializedName
import eryaz.software.zeusBase.data.models.remote.response.BaseResponse

data class ResultModel<T>(
    @SerializedName("result")
    val result: T
) : BaseResponse()