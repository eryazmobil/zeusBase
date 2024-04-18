package eryaz.software.zeusBase.data.models.remote.models

import com.google.gson.annotations.SerializedName

class PdaVersionModel(

    @SerializedName("version")
    val version: String?,

    @SerializedName("downloadLink")
    val downloadLink: String?,

    @SerializedName("id")
    val id: Int?

)

