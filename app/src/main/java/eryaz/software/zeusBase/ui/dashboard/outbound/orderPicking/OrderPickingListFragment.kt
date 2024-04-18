package eryaz.software.zeusBase.ui.dashboard.outbound.orderPicking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.persistence.TemporaryCashManager
import eryaz.software.zeusBase.databinding.FragmentOrderPickingListBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.ui.dashboard.outbound.orderPicking.adapter.OrderWorkActivityListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderPickingListFragment : BaseFragment() {
    override val viewModel by viewModel<OrderPickingListVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentOrderPickingListBinding.inflate(layoutInflater)
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
        viewModel.getActiveWorkAction()

        viewModel.orderPickingList
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        viewModel.searchList()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        viewModel.navigateToDetail
            .asLiveData()
            .observe(viewLifecycleOwner) {
                if (it) {
                    findNavController().navigate(
                        OrderPickingListFragmentDirections.actionOrderPickingListFragmentToOrderPickingDetailFragment()
                    )
                }
            }
    }

    override fun setClicks() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemClick = {
            if (it.isLocked) {
                errorDialog.show(
                    context,
                    ErrorDialogDto(
                        titleRes = R.string.warning,
                        messageRes = R.string.locked_work_activity
                    )
                )
            }
            TemporaryCashManager.getInstance().workActivity = it
            viewModel.getWorkActionForPda()
        }
    }

    private val adapter by lazy {
        OrderWorkActivityListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}