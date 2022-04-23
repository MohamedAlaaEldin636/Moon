package grand.app.moon.presentation.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentFilterBinding
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.viewModels.FilterViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>() {
  val args: FilterFragmentArgs by navArgs()
  private val viewModel: FilterViewModel by viewModels()

  private val TAG = "FilterFragment"

  override
  fun getLayoutId() = R.layout.fragment_filter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
      if (bundle.containsKey(Constants.PROPERTY)) {
        viewModel.updateCallBack(bundle.getSerializable(Constants.PROPERTY) as FilterProperty)

      }
    }
  }

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.request.categoryId = args.categoryId
    viewModel.request.sub_category_id = args.subCategoryId
    viewModel.callService()
  }


  override fun setupObservers() {

    lifecycleScope.launchWhenResumed {
      viewModel.response.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.setData(it.value.data)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    viewModel.adapter.clickEvent.observe(this, {
      when (it.filterType) {
        FILTER_TYPE.SINGLE_SELECT -> findNavController().navigate(
          FilterFragmentDirections.actionFilterFragmentToFilterSingleSelectDialog(
            it,
            it.name
          )
        )
        FILTER_TYPE.CITY, FILTER_TYPE.MULTI_SELECT -> findNavController().navigate(
          FilterFragmentDirections.actionFilterFragmentToFilterMultiSelectDialog(
            it,
            it.name
          )
        )
      }
    })
  }


  override fun onStop() {
    super.onStop()
  }


}