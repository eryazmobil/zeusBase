package eryaz.software.zeusBase.ui.dashboard.query.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.ProductStorageQuantityDto
import eryaz.software.zeusBase.databinding.ItemStorageQuantityTextBinding

class StorageProductQuantityVH(val binding: ItemStorageQuantityTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductStorageQuantityDto,
        isLastItem: Boolean
    ) {
        binding.keyTxt.text = dto.storageText
        binding.valueTxt.text = dto.quantity

        binding.underline.isVisible = !isLastItem
    }

    companion object {
        fun from(parent: ViewGroup): StorageProductQuantityVH {
            val binding = ItemStorageQuantityTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return StorageProductQuantityVH(binding)
        }
    }
}