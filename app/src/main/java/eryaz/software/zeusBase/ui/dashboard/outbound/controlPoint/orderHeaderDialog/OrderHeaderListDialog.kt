package eryaz.software.zeusBase.ui.dashboard.outbound.controlPoint.orderHeaderDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.zeusBase.databinding.OrderHeaderListBinding
import eryaz.software.zeusBase.ui.base.BaseDialogFragment
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.extensions.observe
import eryaz.software.zeusBase.util.extensions.orZero
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OrderHeaderListDialog : BaseDialogFragment() {
    private val safeArgs by navArgs<OrderHeaderListDialogArgs>()

    override val viewModel by viewModel<OrderHeaderListVM> {
        parametersOf(safeArgs.orderHeaderList.toList())
    }
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        OrderHeaderListBinding.inflate(layoutInflater)
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

    override fun setClicks() {
        binding.continuedBtn.setOnSingleClickListener {
            viewModel.orderHeaderDto.observe(this) {
                findNavController().navigate(
                    OrderHeaderListDialogDirections.actionOrderHeaderListDialogToControlPointDetailFragment(
                        it?.workActivity?.workActivityCode.orEmpty(), it?.id.orZero()
                    )
                )
            }
        }
    }
}