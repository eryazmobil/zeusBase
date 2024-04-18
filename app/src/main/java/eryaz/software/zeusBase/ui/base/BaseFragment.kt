package eryaz.software.zeusBase.ui.base

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.enums.SoundEnum
import eryaz.software.zeusBase.data.models.dto.ConfirmationDialogDto
import eryaz.software.zeusBase.util.StatusBarUtil.changeStatusBarMode
import eryaz.software.zeusBase.util.dialogs.ConfirmationDialog
import eryaz.software.zeusBase.util.dialogs.ErrorDialog
import eryaz.software.zeusBase.util.dialogs.ProgressDialog
import eryaz.software.zeusBase.util.dialogs.WarningDialog
import eryaz.software.zeusBase.util.extensions.appCompatActivity
import eryaz.software.zeusBase.util.extensions.changeWindowBackground
import eryaz.software.zeusBase.util.extensions.getColorAttrs
import eryaz.software.zeusBase.util.extensions.getColorCompat
import eryaz.software.zeusBase.util.extensions.getColorInt
import eryaz.software.zeusBase.util.extensions.observe
import eryaz.software.zeusBase.util.extensions.supportActionBar

open class BaseFragment : Fragment() {
    private val _progressDialog = ProgressDialog(lifecycle)
    private val _errorDialog = ErrorDialog(lifecycle)
    private val _warningDialog = WarningDialog(lifecycle)
    private val _confirmationDialog = ConfirmationDialog(lifecycle)

    private val progressDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _progressDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }

    val errorDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _errorDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }

    private val warningDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _warningDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }

    val confirmationDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _confirmationDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }

    protected open val viewModel: BaseViewModel? = null

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel?.showErrorDialog
            ?.observe(this, Lifecycle.State.RESUMED) { errorDialog.show(context, it) }

        viewModel?.showWarningDialog
            ?.observe(this, Lifecycle.State.RESUMED) { warningDialog.show(context, it) }

        viewModel?.showConfirmationDialog
            ?.observe(this, Lifecycle.State.RESUMED) { confirmationDialog.show(context, it) }

        viewModel?.showProgressDialog
            ?.observe(this, Lifecycle.State.RESUMED) { progressDialog.setUiState(it) }

        viewModel?.stringProvider = {
            context?.getString(it).orEmpty()
        }

        setClicks()
        subscribeToObservables()
    }

    override fun onStart() {
        super.onStart()

        initActionBar()
    }

    private fun initActionBar() {
        if (parentFragment is NavHostFragment) {
            val mainActivity = appCompatActivity() as BaseActivity
            mainActivity.changeWindowBackground(drawable = getWindowBackground())
            mainActivity.window.statusBarColor = getStatusBarColor()


            changeStatusBarMode()

            supportActionBar()?.title = supportActionBar()?.title
            supportActionBar()?.subtitle = getSubTitle()

            if (hideActionBar())
                supportActionBar()?.hide()
            else
                supportActionBar()?.show()
        }
    }

    open fun isLightStatusBarText(): Boolean {
        return true
    }

    open fun hideActionBar(): Boolean {
        return false
    }

    open fun getActionBarBackground(): Drawable? {
        return ColorDrawable(context.getColorCompat(R.color.colorButtonRed))
    }

    open fun getWindowBackground(): Drawable? {
        return ColorDrawable(context.getColorAttrs(android.R.attr.colorBackground))
    }

    open fun getSubTitle(): String? = ""

    open fun setClicks() {}

    open fun subscribeToObservables() {}

    open fun playSound(type: SoundEnum) {
        val rowResId = when (type) {
            SoundEnum.Success -> eryaz.software.zeusBase.data.R.raw.success_sound
            SoundEnum.Failure -> eryaz.software.zeusBase.data.R.raw.fail_sound
        }
        MediaPlayer.create(context, rowResId)
            .start()
    }

    open fun getStatusBarColor(): Int {
        return context.getColorInt(R.color.colorPrimaryBoldBlue)
    }

    open fun showConditionDialog(model: ConfirmationDialogDto) {
        confirmationDialog.show(context, model)
    }
}