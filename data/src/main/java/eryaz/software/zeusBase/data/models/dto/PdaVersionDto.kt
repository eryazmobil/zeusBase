package eryaz.software.zeusBase.data.models.dto

import com.google.gson.annotations.SerializedName

data class PdaVersionDto(
    val version: String?,
    val downloadLink: String?,
    val id: Int?,
    val apkZipName : String?,
    val apkFileName : String?
)
