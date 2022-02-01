package app.grand.tafwak.presentation.auth.schoolInfo.grades

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.auth.schoolInfo.grades.viewModels.SchoolGradesViewModel
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.*
import com.structure.base_mvvm.databinding.FragmentSchoolGradesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SchoolGradeFragment : BaseFragment<FragmentSchoolGradesBinding>() {

  private val viewModel: SchoolGradesViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_school_grades

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
      viewModel.stagesResponse.collect {
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
            openStageLevels()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    viewModel.adapter.changeEvent.observeForever { grade ->
      binding.tvSelectedCountry.text = grade.name
    }
  }

  private fun openStageLevels() {
    navigateSafe(
      SchoolGradeFragmentDirections.actionSchoolGradeFragmentToSchoolLevelsFragment2(
        viewModel.adapter.lastSelected
      )
    )
  }
}