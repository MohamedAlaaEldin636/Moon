package grand.app.moon.presentation.group_details

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.databinding.FragmentGroupDetailsBinding
import grand.app.moon.presentation.group_details.viewModels.GroupDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailsFragment : BaseFragment<FragmentGroupDetailsBinding>() {

  private val viewModel: GroupDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_group_details

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }


  override fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.REVIEWS)
        toReviews()
    }
    lifecycleScope.launchWhenResumed {
      viewModel.detailsResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.groupDetails = it.value.data
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }

  private fun toReviews() {
    navigateSafe(
      GroupDetailsFragmentDirections.actionGroupDetailsFragmentToReviewsFragment(
        viewModel.groupDetails.instructor.id
      )
    )
  }
}