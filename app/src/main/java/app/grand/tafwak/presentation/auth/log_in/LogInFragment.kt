package app.grand.tafwak.presentation.auth.log_in

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import app.grand.tafwak.domain.auth.enums.AuthFieldsValidation
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.*
import app.grand.tafwak.presentation.base.utils.getDeviceId
import app.grand.tafwak.presentation.base.utils.showNoApiErrorAlert
import com.structure.base_mvvm.databinding.FragmentLogInBinding
import app.grand.tafwak.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
            openHome()
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
    lifecycleScope.launch {
      viewModel.userLocalUseCase.invoke().collect { user ->

      }
    }

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