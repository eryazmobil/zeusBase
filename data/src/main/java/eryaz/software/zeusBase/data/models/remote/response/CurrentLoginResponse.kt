package eryaz.software.zeusBase.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class CurrentLoginResponse(
    @SerializedName("user")
    val userInfo: CurrentUserResponse
)