package eryaz.software.zeusBase.data.models.remote.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Query

data class StepCountRequest(
    @SerializedName("stepCount")
    val stepCount: Int
)