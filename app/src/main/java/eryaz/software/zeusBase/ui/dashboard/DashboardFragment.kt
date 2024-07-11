package eryaz.software.zeusBase.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eryaz.software.zeusBase.data.R
import eryaz.software.zeusBase.data.enums.DashboardPermissionType.RETURNING
import eryaz.software.zeusBase.data.enums.IconType
import eryaz.software.zeusBase.databinding.FragmentDashboardBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.util.adapter.dashboard.adapters.DashboardAdapter
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.dialogs.QuestionDialog
import eryaz.software.zeusBase.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment() {
    override val viewModel by viewModel<DashboardViewModel>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentDashboardBinding.inflate(layoutInflater)
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
        super.subscribeToObservables()

        viewModel.dashboardItemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private val adapter by lazy {
        DashboardAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    override fun setClicks() {
        super.setClicks()

        adapter.onItemClick = { dto ->
            when (dto.type) {
                RETURNING -> toast(getString(R.string.not_done_yet))
                else -> {
                    val title = requireActivity().getString(dto.titleRes)
                    findNavController().navigate(
                        DashboardFragmentDirections.actionMainFragmentToDashboardDetailFragment(
                            title, dto.type
                        )
                    )
                }
            }
        }

        binding.settingsBtn.setOnSingleClickListener {
            findNavController().navigate(
                DashboardFragmentDirections.actionMainFragmentToNavSettings()
            )
        }
        binding.toolbar.setMenuOnClickListener {
            QuestionDialog(
                onNegativeClickListener = {
                },
                onPositiveClickListener = {
                    viewModel.updateGeneralStepByUserId()
                    findNavController().navigateUp()
                },
                textHeader = resources.getString(R.string.exit),
                textMessage = resources.getString(R.string.are_you_sure),
                positiveBtnText = resources.getString(R.string.yes),
                negativeBtnText = resources.getString(R.string.no),
                singleBtnText = "",
                negativeBtnViewVisible = true,
                icType = IconType.Success.ordinal
            ).show(parentFragmentManager, "dialog")
        }
    }

    override fun hideActionBar() = true
}