package com.structure.base_mvvm.presentation.group_details

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.handleApiError
import com.structure.base_mvvm.presentation.base.extensions.hideKeyboard
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentGroupDetailsBinding
import com.structure.base_mvvm.presentation.group_details.viewModels.GroupDetailsViewModel
import com.structure.base_mvvm.presentation.teachers.TeacherProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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