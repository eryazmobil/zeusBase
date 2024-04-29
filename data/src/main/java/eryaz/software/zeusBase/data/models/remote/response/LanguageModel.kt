package eryaz.software.zeusBase.data.models.remote.response

import androidx.databinding.ObservableField
import eryaz.software.zeusBase.data.enums.LanguageType

data class LanguageModel(
    val lang: LanguageType,
    val isSelected: ObservableField<Boolean>
)