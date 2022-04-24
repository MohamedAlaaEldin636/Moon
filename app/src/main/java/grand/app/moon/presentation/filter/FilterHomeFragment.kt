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
import grand.app.moon.databinding.FragmentFilterHomeBinding
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.viewModels.FilterHomeViewModel
import grand.app.moon.presentation.filter.viewModels.FilterViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilterHomeFragment : BaseFragment<FragmentFilterHomeBinding>() {
  private val viewModel: FilterHomeViewModel by viewModels()

  private val TAG = "FilterHomeFragment"

  override
  fun getLayoutId() = R.layout.fragment_filter_home

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
  }


  override fun setupObservers() {
    viewModel.adapter.clickEvent.observe(this, {
      when (it.filterType) {
        FILTER_TYPE.SINGLE_SELECT, FILTER_TYPE.SORT_BY, FILTER_TYPE.OTHER_OPTIONS, FILTER_TYPE.CATEGORY, FILTER_TYPE.SUB_CATEGORY -> findNavController().navigate(
          FilterHomeFragmentDirections.actionFilterHomeFragmentToFilterSingleSelectDialog(
            it,
            it.name
          )
        )
        FILTER_TYPE.CITY, FILTER_TYPE.MULTI_SELECT -> findNavController().navigate(
          FilterHomeFragmentDirections.actionFilterHomeFragmentToFilterMultiSelectDialog(
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