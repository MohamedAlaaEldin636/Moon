package app.grand.tafwak.presentation.teachers

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.handleApiError
import app.grand.tafwak.presentation.base.extensions.hideKeyboard
import app.grand.tafwak.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.databinding.FragmentTeachersBinding
import app.grand.tafwak.presentation.teachers.viewModels.TeachersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TeachersFragment : BaseFragment<FragmentTeachersBinding>() {

  private val viewModel: TeachersViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teachers

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setupObservers() {
    lifecycleScope.launchWhenResumed {
      viewModel.teacherResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.studentTeacher = it.value.data
            binding.imageSlider.setSliderAdapter(viewModel.homeSliderAdapter)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    viewModel.adapter.clickEvent.observeForever { instructor ->
      toTeacherProfile(instructor.id, instructor.name)
    }
  }

  private fun toTeacherProfile(instructorId: Int, instructorName: String) {
    navigateSafe(
      TeachersFragmentDirections.actionToTeacherProfileFragment(
        instructorId,
        instructorName
      )
    )
  }
}