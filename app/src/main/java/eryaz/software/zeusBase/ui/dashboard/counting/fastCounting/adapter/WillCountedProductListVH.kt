package eryaz.software.zeusBase.ui.dashboard.counting.fastCounting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.CountingComparisonDto
import eryaz.software.zeusBase.databinding.ItemCountingProductListBinding
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener

class WillCountedProductListVH(val binding: ItemCountingProductListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: CountingComparisonDto,
        onItemClick: ((CountingComparisonDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): WillCountedProductListVH {
            val binding = ItemCountingProductListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return WillCountedProductListVH(binding)
        }
    }
}