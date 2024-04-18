package eryaz.software.zeusBase.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class WorkActivityTypeResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("code")
    val code: String,

    @SerializedName("definition")
    val definition: String,
)