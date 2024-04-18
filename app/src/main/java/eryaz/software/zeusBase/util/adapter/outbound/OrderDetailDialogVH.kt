package eryaz.software.zeusBase.util.adapter.outbound

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.OrderDetailDto
import eryaz.software.zeusBase.databinding.ItemOrderDetailListBinding

class OrderDetailDialogVH(val binding: ItemOrderDetailListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dto: OrderDetailDto) {
        binding.dto = dto
        binding.executePendingBindings()
        setControl(dto)
    }

    private fun setControl(dto: OrderDetailDto) {
        val (statusName, statusColor) = when {
            dto.quantityCollected == dto.quantity -> "TAM" to "#4caf50"
            dto.quantityCollected < dto.quantity -> "EKSIK" to "#df0029"
            else -> "FAZLA" to "#ffb822"
        }

        binding.controlTitle.apply {
            text = statusName
            setBackgroundColor(Color.parseColor(statusColor))
        }
    }

    companion object {
        fun from(parent: ViewGroup): OrderDetailDialogVH {
            val binding = ItemOrderDetailListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return OrderDetailDialogVH(binding)
        }
    }
}