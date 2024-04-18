package eryaz.software.zeusBase.ui.dashboard.outbound.datControl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.databinding.FragmentControlPointTransferListBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.util.adapter.outbound.dat.ControlPointTransferListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ControlPointTransferListFragment : BaseFragment() {
    override val viewModel by viewModel<ControlPointTransferListVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentControlPointTransferListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getTransferWorkActivityList()
    }

    override fun subscribeToObservables() {

        viewModel.workActivityList
            .asLiveData()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
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
                        titleRes = R.string.error,
                        messageRes = R.string.locked_work_activity
                    )
                )
            } else {
                findNavController().navigate(
                    ControlPointTransferListFragmentDirections.actionControlPointTransferListFragmentToTransferControlPointDetailFragment(
                        it.workActivityId,
                        it.workActivityCode
                    )
                )

            }
        }
    }

    private val adapter by lazy {
        ControlPointTransferListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}