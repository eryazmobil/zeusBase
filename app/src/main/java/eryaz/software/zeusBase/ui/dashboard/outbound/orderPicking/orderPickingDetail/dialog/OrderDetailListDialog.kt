package eryaz.software.zeusBase.ui.dashboard.outbound.orderPicking.orderPickingDetail.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eryaz.software.zeusBase.databinding.OrderDetailDialogListBinding
import eryaz.software.zeusBase.ui.base.BaseDialogFragment
import eryaz.software.zeusBase.util.adapter.outbound.OrderDetailListDialogAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderDetailListDialog : BaseDialogFragment() {

    override val viewModel by viewModel<OrderDetailListDialogVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        OrderDetailDialogListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        return binding.root
    }

    override fun subscribeToObservables() {

        val displayMetrics = resources.displayMetrics
        val widthPercentage = 0.8
        val heightPercentage = 0.9

        val width = (displayMetrics.widthPixels * widthPercentage).toInt()
        val height = (displayMetrics.heightPixels * heightPercentage).toInt()

        dialog?.window?.setLayout(width, height)

        viewModel.orderDetailList
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        viewModel.searchList()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy {
        OrderDetailListDialogAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}