package eryaz.software.zeusBase.util.adapter.outbound.dat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.WorkActivityDto
import eryaz.software.zeusBase.databinding.ItemControlPointTransferListBinding
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener

class ControlPointTransferListVH(val binding: ItemControlPointTransferListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: WorkActivityDto,
        onItemClick: ((WorkActivityDto) -> Unit)
    ) {
        binding.dto = dto
        binding.executePendingBindings()
        setStatus()

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    private fun setStatus() {
        binding.definition.apply {
            setTextColor(Color.BLACK)
        }
        binding.itemRoot.apply {
            setBackgroundColor(Color.YELLOW)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ControlPointTransferListVH {
            val binding = ItemControlPointTransferListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ControlPointTransferListVH(binding)
        }
    }
}