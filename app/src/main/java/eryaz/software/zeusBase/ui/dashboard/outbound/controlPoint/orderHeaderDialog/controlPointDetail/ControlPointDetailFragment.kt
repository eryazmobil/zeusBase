package eryaz.software.zeusBase.ui.dashboard.outbound.controlPoint.orderHeaderDialog.controlPointDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.databinding.FragmentControlPointDetailBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.util.adapter.outbound.ControlPointDetailListAdapter
import eryaz.software.zeusBase.util.adapter.outbound.ControlPointDetailListVH
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.extensions.hideSoftKeyboard
import eryaz.software.zeusBase.util.extensions.observe
import eryaz.software.zeusBase.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ControlPointDetailFragment : BaseFragment() {
    private val safeArgs by navArgs<ControlPointDetailFragmentArgs>()

    override val viewModel by viewModel<ControlPointDetailVM> {
        parametersOf(safeArgs.workActivityCode, safeArgs.orderHeaderId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentControlPointDetailBinding.inflate(layoutInflater)
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
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.searchProduct.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.controlBtn.setOnSingleClickListener {
            viewModel.addQuantityForControl(viewModel.quantity.value.toInt())
        }

        binding.packageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setSelectedPackagePosition(position)
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }
            }

        binding.detailPackage.setOnSingleClickListener {
            viewModel.getPackageList()
        }

        binding.addPackage.setOnSingleClickListener {
            viewModel.packageList.observe(this) { packageList ->
                findNavController().navigate(
                    ControlPointDetailFragmentDirections.actionControlPointDetailFragmentToPackageListDialog(
                        packageList.toTypedArray(), viewModel.orderHeaderId
                    )
                )
            }
        }

        binding.updatePackage.setOnSingleClickListener {
            if (viewModel.getSelectedPackagePosition() != 0) {
                findNavController().navigate(
                    ControlPointDetailFragmentDirections.actionControlPointDetailFragmentToUpdatePackageControlDialog(
                        viewModel.selectedPackageDto!!, viewModel.selectedPackageId
                    )
                )
            } else {
                toast(getString(R.string.pleaseSelectPackage))
            }
        }
    }

    override fun subscribeToObservables() {
        viewModel.serialCheckBox.observe(this) {
            if (binding.quantityEdt.hasFocus())
                binding.quantityEdt.hideSoftKeyboard()
        }

        viewModel.orderDetailList.observe(this) {
            adapter.submitList(it)
        }

        viewModel.packageList.observe(this) { list ->
            if (list.isNotEmpty()) {
                binding.stateView.setViewVisible(binding.packageSpinner, true)

            }
            context?.let {
                val adapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, list)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.packageSpinner.adapter = adapter
            }
        }

        viewModel.scrollToPosition
            .asLiveData()
            .observe(viewLifecycleOwner) {
                binding.recyclerView.scrollToPosition(it)

                (binding.recyclerView.findViewHolderForAdapterPosition(it) as? ControlPointDetailListVH)?.animateBackground()
            }
    }

    private val adapter by lazy {
        ControlPointDetailListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    override fun onResume() {
        super.onResume()
        binding.searchEdt.requestFocus()
    }
}
