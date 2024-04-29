package eryaz.software.zeusBase.ui.dashboard.settings.changeLanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.databinding.FragmentLanguageBinding
import eryaz.software.zeusBase.ui.base.BaseBottomSheetDialogFragmentKt
import eryaz.software.zeusBase.ui.dashboard.settings.adapter.LanguageAdapterKt
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class LanguageFragment : BaseBottomSheetDialogFragmentKt() {
    override val viewModel by viewModel<LanguageVM>()

    private val binding by lazy {
        FragmentLanguageBinding.inflate(layoutInflater)
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
        binding.closeBtn.setOnSingleClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemClick = {
            SessionManager.language = it.lang
            findNavController().navigateUp()
            activity?.recreate()
            setFragmentResult(
                LANGUAGE_FRAGMENT_TAG, bundleOf(
                    LANGUAGE_FRAGMENT_KEY to it.lang.name
                )
            )
        }
    }

    override fun subscribeToObservables() {
        viewModel.langList
            .asLiveData()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy {
        LanguageAdapterKt().also {
            binding.recyclerView.adapter = it
        }
    }

    companion object {
        const val LANGUAGE_FRAGMENT_TAG = "language_fragment_tag"
        const val LANGUAGE_FRAGMENT_KEY = "language_fragment_key"
    }
}
