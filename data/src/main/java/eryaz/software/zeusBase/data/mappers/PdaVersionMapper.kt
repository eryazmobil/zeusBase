package eryaz.software.zeusBase.data.mappers

import eryaz.software.zeusBase.data.models.dto.PdaVersionDto
import eryaz.software.zeusBase.data.models.remote.models.PdaVersionModel

fun PdaVersionModel.toDto() = PdaVersionDto(
    version = version,
    downloadLink = downloadLink,
    id = id,
    apkZipName = downloadLink?.substringAfterLast("/"),
    apkFileName = downloadLink?.substringAfterLast("/")?.substringBeforeLast(".")
)
