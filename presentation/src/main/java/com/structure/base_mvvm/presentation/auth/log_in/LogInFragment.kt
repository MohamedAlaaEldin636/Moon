package com.structure.base_mvvm.presentation.auth.log_in

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.databinding.FragmentLogInBinding
import com.structure.base_mvvm.presentation.home.HomeActivity
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
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) {
      when {
        Constants.FORGET_PASSWORD == it -> openForgotPassword()
        it == Constants.REGISTER -> openSignUp()
        it == Constants.CONTINUE_PROGRESS -> openContinueDialog()
      }

    }

//    viewModel.validationException.observe(this) {
//      when (it) {
//        AuthFieldsValidation.EMPTY_EMAIL.value -> {
//          requireView().showSnackBar(resources.getString(R.string.empty_email))
//        }
//        AuthFieldsValidation.INVALID_EMAIL.value -> {
//          requireView().showSnackBar(resources.getString(R.string.invalid_email))
//        }
//        AuthFieldsValidation.EMPTY_PASSWORD.value -> {
//          requireView().showSnackBar(resources.getString(R.string.empty_password))
//        }
//      }
//    }

    lifecycleScope.launchWhenResumed {
      viewModel.logInResponse.collect {
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
            handleApiError(it, retryAction = { viewModel.onLogInClicked() })
          }
        }
      }
    }
  }

  private fun openForgotPassword() {
    navigateSafe(LogInFragmentDirections.actionOpenForgotPasswordFragment("01030407100"))
  }

  private fun openSignUp() {
    navigateSafe(LogInFragmentDirections.actionOpenSignUpFragment())
  }

  private fun openHome() {
    requireActivity().openActivityAndClearStack(HomeActivity::class.java)
  }

  private fun openContinueDialog() {
    PrettyPopUpHelper.Builder(childFragmentManager)
      .setStyle(PrettyPopUpHelper.Style.STYLE_1_HORIZONTAL_BUTTONS)
      .setContent(R.string.continue_register_title)
      .setContentColor(getMyColor(R.color.colorPrimaryDark))
      .setPositiveButtonBackground(R.drawable.corner_blue)
      .setPositiveButtonTextColor(getMyColor(R.color.white))
      .setPositiveButton(R.string.yes) {
        it.dismiss()
        if (viewModel.regsiter_step == "2")
          openCountries()
      }
      .setNegativeButtonBackground(R.drawable.btn_gray)
      .setNegativeButtonTextColor(getMyColor(R.color.white))
      .setNegativeButton(getMyString(R.string.cancel), null)
      .create()
  }

  private fun openCountries() {
    navigateSafe(LogInFragmentDirections.actionLogInFragmentToCountriesFragment())
  }
}