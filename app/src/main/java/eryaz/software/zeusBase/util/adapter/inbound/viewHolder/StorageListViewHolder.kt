package eryaz.software.zeusBase.util.adapter.inbound.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.StorageDto
import eryaz.software.zeusBase.databinding.InMovementStorageListItemDialogBinding
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener

class StorageListViewHolder(val binding: InMovementStorageListItemDialogBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: StorageDto,
        onItemClick: ((StorageDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): StorageListViewHolder {
            val binding = InMovementStorageListItemDialogBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return StorageListViewHolder(binding)
        }
    }
}