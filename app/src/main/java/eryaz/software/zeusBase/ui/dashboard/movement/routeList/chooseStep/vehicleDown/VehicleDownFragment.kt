package eryaz.software.zeusBase.ui.dashboard.movement.routeList.chooseStep.vehicleDown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.databinding.FragmentVehicleDownBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.ui.dashboard.counting.adapter.CountingListAdapter
import eryaz.software.zeusBase.ui.dashboard.movement.routeList.chooseStep.ChooseVehicleFragmentArgs
import eryaz.software.zeusBase.ui.dashboard.movement.routeList.chooseStep.ChooseVehicleVM
import eryaz.software.zeusBase.util.adapter.movement.packageList.VehiclePackageAdapter
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.extensions.hideSoftKeyboard
import eryaz.software.zeusBase.util.extensions.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VehicleDownFragment : BaseFragment() {

    private val safeArgs by navArgs<VehicleDownFragmentArgs>()

    override val viewModel by viewModel<VehicleDownVM> {
        parametersOf(safeArgs.vehicleID, safeArgs.routeID)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentVehicleDownBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun subscribeToObservables() {
        viewModel.vehicleDownSuccess.asLiveData().observe(this) {
                if (it) {
                    binding.searchEdt.requestFocus()
                    viewModel.getOrderHeaderRouteList()
                    Toast.makeText(context, getString(R.string.added_package), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        viewModel.packageList.asLiveData().observe(this) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy {
        VehiclePackageAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    override fun setClicks() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemClick = {
            findNavController().navigate(
                VehicleDownFragmentDirections.actionVehicleDownFragmentToOrderDetailViewPagerFragment(
                    viewModel.routeID, it.orderHeaderId
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

        binding.searchEdt.requestFocus()
    }
}