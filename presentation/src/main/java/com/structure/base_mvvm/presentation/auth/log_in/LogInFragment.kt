package com.structure.base_mvvm.presentation.auth.log_in

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.base.utils.getDeviceId
import com.structure.base_mvvm.presentation.base.utils.showNoApiErrorAlert
import com.structure.base_mvvm.presentation.databinding.FragmentLogInBinding
import com.structure.base_mvvm.presentation.home.HomeActivity
import com.structure.base_mvvm.presentation.teachers.home.TeacherHomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>() {

  private val viewModel: LogInViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_log_in

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.request.device_token = getDeviceId(requireActivity())
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) {
      when (it) {
        Constants.FORGET_PASSWORD -> openForgotPassword()
        Constants.REGISTER -> openSignUp()
        Constants.CONTINUE_PROGRESS -> openContinueDialog()
      }

    }

    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.empty_email))
        }
        AuthFieldsValidation.INVALID_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.invalid_email))
        }
        AuthFieldsValidation.EMPTY_PASSWORD.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.empty_password))
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel.logInResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            if (viewModel.checkUserType()!=null)
              openHome()
            else checkNavigate()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(
              it,
              retryAction = { viewModel.onLogInClicked() },
              notActiveAction = { openConfirm() })
          }

        }
      }
    }
  }

  private fun openForgotPassword() {
    navigateSafe(LogInFragmentDirections.actionOpenForgotPasswordFragment())
  }

  private fun openConfirm() {
    navigateSafe(
      LogInFragmentDirections.actionLogInFragmentToFragmentConfirmCode(
        viewModel.request.email,
        Constants.Verify
      )
    )
  }

  private fun openSignUp() {
    navigateSafe(LogInFragmentDirections.actionOpenSignUpFragment())
  }

  private fun openHome() {
    if (viewModel.checkUserType() == Constants.STUDENT_TYPE)
      requireActivity().openActivityAndClearStack(HomeActivity::class.java)
    else
      requireActivity().openActivityAndClearStack(TeacherHomeActivity::class.java)
  }

  private fun openContinueDialog() {
    PrettyPopUpHelper.Builder(childFragmentManager)
      .setStyle(PrettyPopUpHelper.Style.STYLE_2_VERTICAL_BUTTONS)
      .setTitle(R.string.continue_register_title)
      .setTitleColor(getMyColor(R.color.black))
      .setContent(R.string.continue_register_body)
      .setContentColor(getMyColor(R.color.darkGray))
      .setPositiveButtonBackground(R.drawable.corner_blue)
      .setPositiveButtonTextColor(getMyColor(R.color.white))
      .setPositiveButton(R.string.yes) {
        it.dismiss()
        checkNavigate()
      }
      .setNegativeButtonBackground(R.drawable.btn_gray)
      .setNegativeButtonTextColor(getMyColor(R.color.white))
      .setNegativeButton(getMyString(R.string.cancel), null)
      .create()
  }

  private fun checkNavigate() {
    if (viewModel.registerStep == "1" || viewModel.registerStep == "2")
      navigateSafe(LogInFragmentDirections.actionLogInFragmentToCountriesFragment())
    if (viewModel.registerStep == "3" || viewModel.registerStep == "4")
      navigateSafe(LogInFragmentDirections.actionLogInFragmentToSchoolGradeFragment())
  }
}