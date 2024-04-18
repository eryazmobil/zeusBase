package eryaz.software.zeusBase.ui.dashboard.outbound.orderPicking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.models.dto.WorkActivityDto
import eryaz.software.zeusBase.databinding.ItemOrderWorkActivityListBinding
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener

class OrderWorkActivityListVH(val binding: ItemOrderWorkActivityListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: WorkActivityDto,
        onItemClick: ((WorkActivityDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }

        if (dto.hasPriority) {
            binding.root.setBackgroundResource(R.color.colorButtonRed)
            binding.workActivityCode.setBackgroundResource(R.color.white)
            binding.shippingType.setBackgroundResource(R.color.white)
            binding.txtDate.setBackgroundResource(R.color.white)
            binding.clientName.setBackgroundResource(R.color.white)
            binding.controlPointTxt.setBackgroundResource(R.color.white)
        }
    }

    companion object {
        fun from(parent: ViewGroup): OrderWorkActivityListVH {
            val binding = ItemOrderWorkActivityListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return OrderWorkActivityListVH(binding)
        }
    }
}