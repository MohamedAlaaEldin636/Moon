package com.structure.base_mvvm.presentation.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.databinding.FragmentHomeBinding
import com.structure.base_mvvm.presentation.home.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

  private val viewModel: HomeViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_home

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel

  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.TEACHERS)
        toTeachers()
    }
    lifecycleScope.launchWhenResumed {
      viewModel.homeResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.homeStudentData = it.value.data
            binding.imageSlider.setSliderAdapter(viewModel.homeSliderAdapter)
          }
          is Resource.Failure -> {
            hideLoading()
//            handleApiError(it)
          }
        }
      }
    }
    viewModel.adapter.clickEvent.observeForever { instructor ->
      toTeacherProfile(instructor.id, instructor.name)
    }
    viewModel.groupsAdapter.clickEvent.observeForever { group ->
      toGroupDetails(group.id, group.name)
    }

  }

  private fun toTeachers() {
    ((requireActivity() as HomeActivity)).binding.bottomNavigationView.selectedItemId =
      R.id.teachersFragment
  }

  private fun toTeacherProfile(instructorId: Int, instructorName: String) {
    navigateSafe(
      HomeFragmentDirections.actionToTeacherProfileFragment(
        instructorId,
        instructorName
      )
    )
  }

  private fun toGroupDetails(groupId: Int, groupName: String) {
    navigateSafe(
      HomeFragmentDirections.actionHomeFragmentToGroupDetailsFragment(
        groupId,
        groupName
      )
    )
  }

}