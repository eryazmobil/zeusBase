package eryaz.software.zeusBase.data.models.dto

import android.view.View
import androidx.annotation.StringRes
import eryaz.software.zeusBase.data.R

data class ButtonDto(
    @StringRes var text: Int = R.string.ok,
    var onClickListener: View.OnClickListener? = null
)
