package eryaz.software.zeusBase.util.adapter.outbound.dat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.OrderDetailDto
import eryaz.software.zeusBase.data.models.dto.TransferRequestDetailDto
import eryaz.software.zeusBase.databinding.ItemControlPointDetailBinding
import eryaz.software.zeusBase.databinding.ItemTransferControlPointDetailBinding

class TransferControlPointDetailListVH(
    val binding: ItemTransferControlPointDetailBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: TransferRequestDetailDto
    ) {
        binding.dto = dto
        binding.executePendingBindings()

        setStatus(dto)
    }

    fun animateBackground() {
        binding.backgroundView.animate().alpha(1f).withEndAction {
            binding.backgroundView.animate().alpha(0f)
        }
    }

    private fun setStatus(dto: TransferRequestDetailDto) {
        if (dto.quantityPicked.toInt() == dto.quantityShipped.toInt() && dto.quantityShipped.toInt() != 0) {
            binding.itemParent.setBackgroundColor(Color.parseColor("#6CC654"))
        }
    }

    companion object {
        fun from(parent: ViewGroup): TransferControlPointDetailListVH {
            val binding = ItemTransferControlPointDetailBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return TransferControlPointDetailListVH(binding)
        }
    }
}