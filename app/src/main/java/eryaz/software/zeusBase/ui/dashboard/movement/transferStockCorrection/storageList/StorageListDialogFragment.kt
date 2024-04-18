package eryaz.software.zeusBase.ui.dashboard.movement.transferStockCorrection.storageList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.zeusBase.databinding.DialogStorageListBinding
import eryaz.software.zeusBase.ui.base.BaseDialogFragment
import eryaz.software.zeusBase.util.adapter.inbound.adapter.StorageListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorageListDialogFragment : BaseDialogFragment() {
    private val safeArgs by navArgs<StorageListDialogFragmentArgs>()

    override val viewModel by viewModel<StorageListDialogVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DialogStorageListBinding.inflate(layoutInflater)
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
        adapter.onItemClick = {
            setFragmentResult(
                REQUEST_KEY, bundleOf(
                    ARG_STORAGE_DTO to it,
                    ARG_ENTER_STORAGE to safeArgs.enterStorage
                )
            )
            findNavController().navigateUp()
        }
    }

    override fun subscribeToObservables() {
        viewModel.storageList.asLiveData()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        StorageListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    companion object {
        const val REQUEST_KEY = "StorageListDialogFragment"
        const val ARG_STORAGE_DTO = "storage_dto"
        const val ARG_ENTER_STORAGE = "enter_storage"
    }
}