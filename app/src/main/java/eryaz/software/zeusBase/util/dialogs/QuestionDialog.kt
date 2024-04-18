package eryaz.software.zeusBase.util.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.enums.IconType
import eryaz.software.zeusBase.databinding.DialogQuestionMessageBinding
import eryaz.software.zeusBase.ui.base.BaseDialogFragment
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.extensions.getColorInt
import eryaz.software.zeusBase.util.extensions.isNetworkAvailable

class QuestionDialog(
    private val onPositiveClickListener: (() -> Unit) = {},
    private val onNegativeClickListener: (() -> Unit) = {},
    private val textHeader: String?,
    private val textMessage: String?,
    private val positiveBtnText: String = "",
    private val negativeBtnText: String = "",
    private val singleBtnText:String = "",
    private val negativeBtnViewVisible: Boolean,
    private val icType: Int
) : BaseDialogFragment() {

    private lateinit var binding: DialogQuestionMessageBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogQuestionMessageBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.btnDialogPositive.setOnSingleClickListener {
            onPositiveClickListener.invoke()
            dismiss()
        }

        binding.btnDialogNegative.setOnSingleClickListener {
            onNegativeClickListener.invoke()
            dismiss()
        }

        binding.btnDialogSingle.text = singleBtnText
        binding.txtDialogMessage.text = textMessage
        binding.txtDialogHeader.text = textHeader
        binding.btnDialogNegative.text = negativeBtnText
        binding.btnDialogPositive.text = positiveBtnText
        show(context)
        if (!negativeBtnViewVisible) {
            binding.btnDialogSingle.visibility = View.VISIBLE
            binding.btnDialogSingle.setOnSingleClickListener {
                dismiss()
            }
        } else {
            binding.btnDialogNegative.visibility = View.VISIBLE
        }
        when (icType) {
            IconType.Danger.ordinal -> {
                binding.icDialogStatus.setImageResource(R.drawable.ic_danger)
                context?.resources?.let {
                    binding.txtDialogHeader.setTextColor(
                        it.getColor(
                            R.color.colorDangerRed,
                            requireContext().theme
                        )
                    )
                }
                binding.btnDialogPositive.setBackgroundColor(context.getColorInt(R.color.colorPrimaryBoldBlue))
            }
            IconType.Warning.ordinal -> {
                binding.icDialogStatus.setImageResource(R.drawable.ic_picking_warning)
                context?.resources?.let {
                    binding.txtDialogHeader.setTextColor(
                        it.getColor(
                            R.color.colorPrimaryYellow,
                            requireContext().theme
                        )
                    )
                }
                binding.btnDialogPositive.setBackgroundColor(context.getColorInt(R.color.colorPrimaryBoldBlue))
            }
            IconType.Success.ordinal -> {
                binding.icDialogStatus.setImageResource(R.drawable.ic_done)
                context?.resources?.let {
                    binding.txtDialogHeader.setTextColor(
                        it.getColor(
                            R.color.colorSuccessGreen,
                            requireContext().theme
                        )
                    )
                }
                binding.btnDialogPositive.setBackgroundColor(context.getColorInt(R.color.colorPrimaryBoldBlue))
            }

            else -> dismiss()
        }
        val dialog = builder.create()
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        return dialog
    }

    fun show(context: Context?) {
        if (!context.isNetworkAvailable()) {
            binding.txtDialogMessage.setText(R.string.in_error_dialog_no_internet_connection)
        }
        dialog?.show()
    }


    override fun dismiss() {
        dialog?.dismiss()
    }

}