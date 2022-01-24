package com.structure.base_mvvm.presentation.teachers

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.handleApiError
import com.structure.base_mvvm.presentation.base.extensions.hideKeyboard
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentTeachersBinding
import com.structure.base_mvvm.presentation.teachers.viewModels.TeachersViewModel
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