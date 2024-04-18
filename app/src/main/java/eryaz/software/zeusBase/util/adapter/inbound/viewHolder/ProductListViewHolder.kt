package eryaz.software.zeusBase.util.adapter.inbound.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.ProductDto
import eryaz.software.zeusBase.databinding.ItemDiaglogBinding
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener

class ProductListViewHolder(val binding: ItemDiaglogBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductDto,
        onItemClick: ((ProductDto) -> Unit)
    ) {

        binding.itemText.text = dto.code

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ProductListViewHolder {
            val binding = ItemDiaglogBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ProductListViewHolder(binding)
        }
    }
}