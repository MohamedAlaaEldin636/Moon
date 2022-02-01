package app.grand.tafwak.presentation.teachers

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.handleApiError
import app.grand.tafwak.presentation.base.extensions.hideKeyboard
import app.grand.tafwak.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.databinding.FragmentTeacherProfileBinding
import app.grand.tafwak.presentation.teachers.viewModels.TeacherProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TeacherProfileFragment : BaseFragment<FragmentTeacherProfileBinding>() {

  private val viewModel: TeacherProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teacher_profile

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.REVIEWS)
        toReviews(viewModel.teacherProfile.id)
    }
    lifecycleScope.launchWhenResumed {
      viewModel.profileResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.teacherProfile = it.value.data
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    viewModel.groupsAdapter.clickEvent.observeForever { group ->
      toGroupDetails(group.id, group.name)
    }
  }

  private fun toGroupDetails(groupId: Int, groupName: String) {
    navigateSafe(
      TeacherProfileFragmentDirections.actionTeacherProfileFragmentToGroupDetailsFragment(
        groupId,
        groupName
      )
    )
  }

  private fun toReviews(instructor: Int) {
    navigateSafe(TeacherProfileFragmentDirections.actionToReviewsFragment(instructor))
  }
}