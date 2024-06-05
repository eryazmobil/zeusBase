package eryaz.software.zeusBase.ui.dashboard.counting.firstCounting.firstCountingDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.models.dto.ButtonDto
import eryaz.software.zeusBase.data.models.dto.ConfirmationDialogDto
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.databinding.FragmentFirstCountingDetailBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.extensions.hideSoftKeyboard
import eryaz.software.zeusBase.util.extensions.observe
import eryaz.software.zeusBase.util.extensions.onBackPressedCallback
import eryaz.software.zeusBase.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FirstCountingDetailFragment : BaseFragment() {
    private val safeArgs by navArgs<FirstCountingDetailFragmentArgs>()

    override val viewModel by viewModel<FirstCountingDetailVM> {
        parametersOf(safeArgs.stockTakingHeaderId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentFirstCountingDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun setClicks() {
        binding.shelfAddressEdt.requestFocus()

        onBackPressedCallback {
            showConditionDialog(
                ConfirmationDialogDto(
                    title = getString(R.string.exit),
                    message = getString(R.string.are_you_sure),
                    positiveButton = ButtonDto(text = R.string.yes, onClickListener = {
                        backToPage()
                    }),
                    negativeButton = ButtonDto(
                        text = R.string.no,
                        onClickListener = { confirmationDialog.dismiss() })
                )
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            backToPage()
        }

        binding.toolbar.setMenuOnClickListener {
            //popupMenu(it)
        }

        binding.shelfAddressEdt.setOnEditorActionListener { _, actionId, _ ->

            val isValidBarcode = viewModel.searchShelf.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getShelfByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.searchProductEdt.setOnEditorActionListener { _, actionId, _ ->

            val isValidBarcode = viewModel.searchProduct.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.infoBtn.setOnSingleClickListener {
            findNavController().navigate(
                FirstCountingDetailFragmentDirections.actionFirstCountingDetailFragmentToInfoFirstCountingFragment(
                    viewModel.stHeaderId, viewModel.assignedShelfId
                )
            )
        }
    }

    override fun subscribeToObservables() {

        viewModel.readShelfBarcode.asLiveData().observe(this) {
            if (it) {
                binding.searchProductEdt.requestFocus()
            }
        }

        viewModel.hasNotProductBarcode.asLiveData().observe(this) {
            if (it) {
                errorDialog.show(
                    context, ErrorDialogDto(
                        titleRes = R.string.error,
                        messageRes = R.string.msg_no_barcode_and_new_barcode,
                        positiveButton = ButtonDto(text = R.string.yes, onClickListener = {
                            findNavController().navigate(
                                FirstCountingDetailFragmentDirections.actionFirstCountingDetailFragmentToCreateBarcodeDialog()
                            )
                            errorDialog.dismiss()
                        }),
                        negativeButton = ButtonDto(text = R.string.no, onClickListener = {
                            errorDialog.dismiss()
                            toast(getString(R.string.process_cancelled))
                        })
                    )
                )
            }
        }

        viewModel.showProductDetail.observe(this) {
            if (it) {
                binding.quantityEdt.requestFocus()
            }
        }

        viewModel.productDetail.asLiveData().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.quantityEdt.requestFocus()
            }
        }

        viewModel.actionAddProduct.asLiveData().observe(this) {
            if (it) binding.searchProductEdt.requestFocus()
            toast(getString(R.string.msg_process_success))
        }

        viewModel.actionIsFinished.asLiveData().observe(this) {
            if (it)
                binding.shelfAddressEdt.requestFocus()
        }

        viewModel.actionProcess.asLiveData().observe(viewLifecycleOwner) {
            binding.saveBtn.visibility = View.VISIBLE
            when {
                viewModel.checkProductHasControlled() -> {
                    errorDialog.show(
                        context, ErrorDialogDto(
                            titleRes = R.string.attached_product,
                            messageRes = R.string.msg_attached_before,
                            positiveButton = ButtonDto(text = R.string.add_on, onClickListener = {
                                viewModel.addProduct(true)
                                errorDialog.dismiss()
                            }),
                            negativeButton = ButtonDto(
                                text = R.string.save_last_entered_quantity,
                                onClickListener = {
                                    viewModel.addProduct(false)
                                    errorDialog.dismiss()
                                })
                        )
                    )
                }

                else -> viewModel.addProduct(false)
            }
        }
    }

    private fun backToPage() {
        findNavController().navigateUp()
    }
}



