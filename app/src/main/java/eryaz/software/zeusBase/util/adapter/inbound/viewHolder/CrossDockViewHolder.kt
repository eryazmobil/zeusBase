package eryaz.software.zeusBase.util.adapter.inbound.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.CrossDockDto
import eryaz.software.zeusBase.databinding.ItemCrossDockListBinding
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener

class CrossDockViewHolder(val binding: ItemCrossDockListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: CrossDockDto,
        onItemClick: ((CrossDockDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): CrossDockViewHolder {
            val binding = ItemCrossDockListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return CrossDockViewHolder(binding)
        }
    }
}