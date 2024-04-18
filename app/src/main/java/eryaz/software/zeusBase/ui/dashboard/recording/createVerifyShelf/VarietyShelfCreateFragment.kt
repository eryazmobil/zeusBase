package eryaz.software.zeusBase.ui.dashboard.recording.createVerifyShelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.models.dto.ProductDto
import eryaz.software.zeusBase.databinding.FragmentVarietyShelfCreateBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.zeusBase.ui.dashboard.recording.recordBarcode.BarcodeRecordingFragmentDirections
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.extensions.hideSoftKeyboard
import eryaz.software.zeusBase.util.extensions.observe
import eryaz.software.zeusBase.util.extensions.parcelable
import eryaz.software.zeusBase.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class VarietyShelfCreateFragment : BaseFragment() {
    override val viewModel by viewModel<VarietyShelfCreateVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentVarietyShelfCreateBinding.inflate(layoutInflater)
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

        binding.productBarcodeEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.productBarcode.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.shelfAddressEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.shelfAddress.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getShelfAddressFromApi()
            }

            hideSoftKeyboard()
            true
        }

        binding.productBarcodeLyt.setEndIconOnClickListener {
            findNavController().navigate(
                BarcodeRecordingFragmentDirections.actionBarcodeRecordingFragmentToProductListDialogFragment()
            )
        }

        binding.createBtn.setOnSingleClickListener {
            viewModel.createShelfInsert()
        }

        binding.updateBtn.setOnSingleClickListener {
            viewModel.updateProductShelf()
        }
        binding.deleteBtn.setOnSingleClickListener {
            viewModel.deleteProductShelf()
        }
    }

    override fun subscribeToObservables() {

        setFragmentResultListener(ProductListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<ProductDto>(ProductListDialogFragment.ARG_PRODUCT_DTO)
            dto?.let {
                viewModel.setEnteredProduct(it)
            }
        }

        viewModel.showProductDetail.observe(this) {
            if (it) {
                binding.productBarcodeEdt.requestFocus()
            }
        }

        viewModel.shelfSuccess.observe(this) {
            if (it) {
                binding.amountLyt.requestFocus()
            }
        }

        viewModel.createSuccess.asLiveData()
            .observe(viewLifecycleOwner) {
                if(it){
                    toast(getString(R.string.msg_process_success))
                    binding.productBarcodeEdt.requestFocus()
                }
            }

        viewModel.showProductDetail.observe(this) {
            if (it) {
                binding.shelfAddressEdt.requestFocus()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.productBarcodeEdt.requestFocus()
    }

    override fun hideActionBar() = false
}