package eryaz.software.zeusBase.data.models.remote.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("currentPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
)