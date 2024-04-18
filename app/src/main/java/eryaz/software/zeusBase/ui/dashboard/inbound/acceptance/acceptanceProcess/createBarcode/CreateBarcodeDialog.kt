package eryaz.software.zeusBase.ui.dashboard.inbound.acceptance.acceptanceProcess.createBarcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.asLiveData
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.enums.IconType
import eryaz.software.zeusBase.databinding.CreateBarcodeDialogBinding
import eryaz.software.zeusBase.ui.base.BaseDialogFragment
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.dialogs.QuestionDialog
import eryaz.software.zeusBase.util.extensions.hideSoftKeyboard
import eryaz.software.zeusBase.util.extensions.showLottieAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateBarcodeDialog : BaseDialogFragment() {
    override val viewModel by viewModel<CreateBarcodeDialogVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        CreateBarcodeDialogBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputProductCode.requestFocus()

        binding.inputProductCode.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.productCode.value.trim().isNotEmpty()
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getProductByCode()
            }

            hideSoftKeyboard()
            true
        }

        subscribe()

        binding.saveBtn.setOnSingleClickListener {
            viewModel.createBarcode()
        }

        viewModel.showErrorProductMessage
            .asLiveData()
            .observe(viewLifecycleOwner) {
                QuestionDialog(
                    textHeader = resources.getString(eryaz.software.zeusBase.data.R.string.attention),
                    textMessage = it,
                    singleBtnText = resources.getString(eryaz.software.zeusBase.data.R.string.okay),
                    negativeBtnViewVisible = false,
                    icType = IconType.Warning.ordinal
                ).show(parentFragmentManager, "dialog")
            }

        viewModel.showSuccess
            .asLiveData()
            .observe(viewLifecycleOwner) {
                showLottieAnimation(R.raw.lottie_product_updated)
                dismiss()
            }

    }

    private fun subscribe() {
        viewModel.showErrorProduct
            .asLiveData()
            .observe(viewLifecycleOwner) {
                QuestionDialog(
                    textHeader = resources.getString(eryaz.software.zeusBase.data.R.string.attention),
                    textMessage = resources.getString(eryaz.software.zeusBase.data.R.string.msg_no_no_product),
                    singleBtnText = resources.getString(eryaz.software.zeusBase.data.R.string.okay),
                    negativeBtnViewVisible = false,
                    icType = IconType.Warning.ordinal
                ).show(parentFragmentManager, "dialog")

            }

        viewModel.showProductViewDetail
            .asLiveData()
            .observe(viewLifecycleOwner) {
                binding.inputProductBarcode.requestFocus()
            }
    }

}
