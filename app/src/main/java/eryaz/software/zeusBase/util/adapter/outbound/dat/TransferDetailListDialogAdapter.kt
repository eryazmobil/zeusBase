package eryaz.software.zeusBase.util.adapter.outbound.dat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.zeusBase.data.models.dto.TransferRequestDetailDto

class TransferDetailListDialogAdapter :
    ListAdapter<TransferRequestDetailDto, RecyclerView.ViewHolder>(WaybillDetailDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransferDetailDialogVH.from(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransferDetailDialogVH -> holder.bind(getItem(position))
        }
    }
}

object WaybillDetailDiffCallBack : DiffUtil.ItemCallback<TransferRequestDetailDto>() {
    override fun areItemsTheSame(
        oldItem: TransferRequestDetailDto,
        newItem: TransferRequestDetailDto
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TransferRequestDetailDto,
        newItem: TransferRequestDetailDto
    ) = oldItem == newItem
}
