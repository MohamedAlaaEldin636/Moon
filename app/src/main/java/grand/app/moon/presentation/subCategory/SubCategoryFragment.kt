package grand.app.moon.presentation.subCategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentSubCategoryBinding
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.subCategory.viewModel.SubCategoryViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SubCategoryFragment : BaseFragment<FragmentSubCategoryBinding>() {

  private val args : SubCategoryFragmentArgs by navArgs()
  private val viewModel: SubCategoryViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_sub_category

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
      if(bundle.containsKey(Constants.SORT_BY)) {
        viewModel.sortBy = bundle.getInt(Constants.SORT_BY)
        viewModel.reset()
        viewModel.callService()
      }
    }
  }

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.subCategoryId = args.subCategoryId
    viewModel.setCategoryId()
    viewModel.callService()
  }

  private val TAG = "SubCategoryFragment"

  override
  fun setupObservers() {
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
  }


}