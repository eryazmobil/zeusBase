package eryaz.software.zeusBase.ui.dashboard.query.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.ProductStorageQuantityDto

class StorageProductQuantityAdapter :
    ListAdapter<ProductStorageQuantityDto, RecyclerView.ViewHolder>(DiffCallBacks) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StorageProductQuantityVH.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StorageProductQuantityVH -> {
                holder.bind(
                    dto = getItem(position),
                    isLastItem = position == itemCount.minus(1)
                )
            }
        }
    }
}

object DiffCallBacks : DiffUtil.ItemCallback<ProductStorageQuantityDto>() {
    override fun areItemsTheSame(
        oldItem: ProductStorageQuantityDto,
        newItem: ProductStorageQuantityDto
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ProductStorageQuantityDto,
        newItem: ProductStorageQuantityDto
    ) = oldItem == newItem
}