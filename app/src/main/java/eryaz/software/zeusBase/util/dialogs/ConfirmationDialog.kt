package eryaz.software.zeusBase.util.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.models.dto.ConfirmationDialogDto
import eryaz.software.zeusBase.databinding.DialogConfirmationMessageBinding
import eryaz.software.zeusBase.util.extensions.isNetworkAvailable

class ConfirmationDialog(lifecycle: Lifecycle) : LifecycleEventObserver {
    private lateinit var binding: DialogConfirmationMessageBinding

    private var model: ConfirmationDialogDto? = null
    private var dialog: Dialog? = null

    init {
        lifecycle.addObserver(this)
    }

    fun createDialog(context: Context, layoutInflater: LayoutInflater): ConfirmationDialog {
        binding = DialogConfirmationMessageBinding.inflate(layoutInflater)

        dialog = Dialog(context, R.style.AlertDialogTheme).apply {
            setContentView(binding.root)
            setOnCancelListener {
                model?.dialogCanceled?.invoke()
                model?.showDialog = false
            }
            setOnDismissListener {
                model?.dialogCanceled?.invoke()
                model?.showDialog = false
            }

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes?.windowAnimations = R.style.AlertDialogTheme
            }
        }
        return this
    }

    fun show(context: Context?, model: ConfirmationDialogDto?) {
        model?.let {
            if (!context.isNetworkAvailable()) {
                model.messageRes =
                    R.string.in_error_dialog_no_internet_connection

                if (!model.showNoInternetDialog)
                    return
            }

            if (model.titleRes > 0)
                model.title = context?.getString(model.titleRes)

            if (model.messageRes > 0)
                model.message = context?.getString(model.messageRes)

            if (model.title.isNullOrEmpty() && model.message.isNullOrEmpty())
                return

            this.model = model
            binding.model = model

            if (model.positiveButton.onClickListener == null)
                model.positiveButton.onClickListener = View.OnClickListener { dismiss() }

            if (model.negativeButton.onClickListener == null)
                model.negativeButton.onClickListener = View.OnClickListener { dismiss() }

            dialog?.setCancelable(model.cancelable)

            show()
        }
    }

    private fun show() {
        if (model?.showDialog == true)
            dialog?.show()
    }

    fun dismiss() {
        if (dialog?.isShowing == true)
            dialog?.dismiss()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME ->
                show()

            Lifecycle.Event.ON_STOP ->
                dismiss()

            else -> {}
        }
    }
}