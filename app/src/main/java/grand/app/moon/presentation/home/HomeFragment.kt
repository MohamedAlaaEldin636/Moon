package grand.app.moon.presentation.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import com.structure.base_mvvm.databinding.FragmentHomeBinding
import grand.app.moon.presentation.home.viewModels.HomeViewModel
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
<<<<<<< HEAD
            viewModel.homeStudentData = it.value.data
//            binding.imageSlider.setSliderAdapter(viewModel.homeSliderAdapter)
=======
>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a
          }
          is Resource.Failure -> {
            hideLoading()
//            handleApiError(it)
          }
        }
      }
    }
<<<<<<< HEAD
//    viewModel.adapter.clickEvent.observeForever { instructor ->
//      toTeacherProfile(instructor.id, instructor.name)
//    }
//    viewModel.groupsAdapter.clickEvent.observeForever { group ->
//      toGroupDetails(group.id, group.name)
//    }
=======
    lifecycleScope.launchWhenResumed {
      viewModel.homeCachResponse.collect {
        when (it) {
          null -> {
            showLoading()
          }
          else -> {
            viewModel.homeStudentData = it
            binding.imageSlider.setSliderAdapter(viewModel.homeSliderAdapter)
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
>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a

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