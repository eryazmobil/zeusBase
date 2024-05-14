package eryaz.software.zeusBase.ui.dashboard.movement.supply.createSupplyWorkActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.models.dto.ErrorDialogDto
import eryaz.software.zeusBase.data.models.dto.SupplyProductShelfDapperDto
import eryaz.software.zeusBase.databinding.FragmentCreateSupplyWorkActivityAndShelfListInformationBinding
import eryaz.software.zeusBase.ui.base.BaseFragment
import eryaz.software.zeusBase.util.adapter.movement.supply.suplyDapperShelfAdapter.SupplyDapperShelfAdapter
import eryaz.software.zeusBase.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.zeusBase.util.extensions.observe
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateSupplyWorkActivityAndShelfListInformation : BaseFragment() {

    override val viewModel by viewModel<CreateSupplyWorkActivityVM>()
    private val args: CreateSupplyWorkActivityAndShelfListInformationArgs by navArgs()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentCreateSupplyWorkActivityAndShelfListInformationBinding.inflate(layoutInflater)
    }

    private val productShelfIdList = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }


    override fun subscribeToObservables() {

        viewModel.getReportProductShelfListForWorkActivityForPda(args.supplyStatus)

        viewModel.supplyProductShelfDapperDtoList.observe(this) {
            adapter.submitList(it)
        }

        viewModel.finishThePage.observe(this){
            if (it) {
                findNavController().navigateUp()
            }
        }

    }

    override fun setClicks() {

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemChecked = {

            if (it.isChecked) {
                productShelfIdList.add(it.productId)
            } else {
                productShelfIdList.remove(it.productId)
            }

        }

        binding.createWorkActivityForSupply.setOnSingleClickListener {

            if (productShelfIdList.isEmpty()) {
                errorDialog.show(
                    requireContext(),
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.must_select_product
                    )
                )
            }
            else{
                viewModel.createProductShelfWorkActivityForPda(
                    args.supplyStatus,
                    productShelfIdList
                )
            }
        }

    }

    private val adapter by lazy {
        SupplyDapperShelfAdapter(requireContext()).also {
            binding.supplyShelfDapperRecycler.adapter = it
        }
    }


}