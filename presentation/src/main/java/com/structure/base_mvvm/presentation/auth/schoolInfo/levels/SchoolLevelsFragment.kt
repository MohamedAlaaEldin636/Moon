package com.structure.base_mvvm.presentation.auth.schoolInfo.levels

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.auth.schoolInfo.grades.SchoolGradeFragmentDirections
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.databinding.FragmentSchoolGradesBinding
import com.structure.base_mvvm.presentation.databinding.FragmentSchoolLevelsBinding
import com.structure.base_mvvm.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SchoolLevelsFragment : BaseFragment<FragmentSchoolLevelsBinding>() {

  private val viewModel: SchoolLevelsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_school_levels

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    binding.includedToolbar.tvTitle.text = getString(R.string.sign_up)
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) {
      if (it == Constants.BACK)
        backToPreviousScreen()
    }
    lifecycleScope.launchWhenResumed {
      viewModel.gradeResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.updateAdapter(it.value.data)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    lifecycleScope.launchWhenResumed {
      viewModel.registerResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            openHome()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    viewModel.adapter.changeEvent.observeForever { levels ->
      binding.tvSelectedCountry.text = levels.name
    }
  }

  private fun openHome() {
    openActivityAndClearStack(HomeActivity::class.java)
  }
}