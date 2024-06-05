package eryaz.software.zeusBase.ui.dashboard.query.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.ProductShelfQuantityDto
import eryaz.software.zeusBase.databinding.ItemStorageQuantityTextBinding

class ShelfProductQuantityVH(val binding: ItemStorageQuantityTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductShelfQuantityDto,
        isLastItem: Boolean,
        showProductInfo: Boolean
    ) {
        if (showProductInfo) {
            binding.keyTxt.text = dto.product.code
        } else {
            binding.keyTxt.text = dto.shelf?.shelfAddress
        }
        binding.valueTxt.text = dto.quantity
        binding.underline.isVisible = !isLastItem
    }

    companion object {
        fun from(parent: ViewGroup): ShelfProductQuantityVH {
            val binding = ItemStorageQuantityTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ShelfProductQuantityVH(binding)
        }
    }
}