package eryaz.software.zeusBase.ui.dashboard.query.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.ProductSpecialShelfDto
import eryaz.software.zeusBase.databinding.ItemStorageQuantityTextBinding

class VarietyProductVH(val binding: ItemStorageQuantityTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductSpecialShelfDto,
        isLastItem: Boolean
    ) {
        binding.keyTxt.text = dto.product.code
        binding.valueTxt.text = dto.quantity

        binding.underline.isVisible = !isLastItem
    }

    companion object {
        fun from(parent: ViewGroup): VarietyProductVH {
            val binding = ItemStorageQuantityTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return VarietyProductVH(binding)
        }
    }
}