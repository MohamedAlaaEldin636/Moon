package app.grand.tafwak.presentation.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.*
import com.structure.base_mvvm.databinding.FragmentHomeBinding
import app.grand.tafwak.presentation.home.viewModels.HomeViewModel
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