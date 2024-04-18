package eryaz.software.zeusBase.ui.dashboard.movement.routeList.chooseStep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.zeusBase.databinding.FragmentChoosenVehicleBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.ui.dashboard.outbound.controlPoint.orderHeaderDialog.controlPointDetail.ControlPointDetailFragmentArgs
import eryaz.software.zeusBase.ui.dashboard.outbound.controlPoint.orderHeaderDialog.controlPointDetail.ControlPointDetailVM
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChooseVehicleFragment : BaseFragment() {

    private val safeArgs by navArgs<ChooseVehicleFragmentArgs>()

    override val viewModel by viewModel<ChooseVehicleVM> {
        parametersOf(safeArgs.routeId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentChoosenVehicleBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun setClicks() {
        binding.downBtn.setOnSingleClickListener {
            findNavController().navigate(
                ChooseVehicleFragmentDirections.actionChooseVehicleFragmentToVehicleDownFragment(
                    1, viewModel.routeId
                )
            )
        }

        binding.upBtn.setOnClickListener {
            findNavController().navigate(
                ChooseVehicleFragmentDirections.actionChooseVehicleFragmentToVehicleUpFragment(
                    2, viewModel.routeId
                )
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
