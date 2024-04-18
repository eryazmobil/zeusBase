package eryaz.software.zeusBase.data.models.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import eryaz.software.zeusBase.data.enums.DashboardDetailPermissionType

data class DashboardDetailItemDto(
    @DrawableRes
    val iconRes:Int,
    @StringRes
    val titleRes: Int,
    var type: DashboardDetailPermissionType,
    var hasPermission: ObservableField<Boolean>
)