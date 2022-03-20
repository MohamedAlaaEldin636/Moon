package grand.app.moon.presentation.teachers.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import com.structure.base_mvvm.databinding.FragmentTeacherHomeBinding
import grand.app.moon.presentation.teachers.home.viewModels.TeacherHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherHomeFragment : BaseFragment<FragmentTeacherHomeBinding>() {

  private val viewModel: TeacherHomeViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teacher_home

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.TEACHER_PROFILE)
        toTeacherProfile()
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
            viewModel.homePaginateData = it.value.data
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    lifecycleScope.launchWhenResumed {
      viewModel.adapter.liveData.collect {
        Log.e("setupObservers", "setupObservers: ")
      }
    }
  }

  private fun toTeacherProfile() {
//    navigateSafe(HomeFragmentDirections.actionToTeacherProfileFragment())
  }


}