package grand.app.moon.presentation.auth.log_in

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import com.google.firebase.components.Dependency.required
import grand.app.moon.domain.auth.enums.AuthFieldsValidation
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.getDeviceId
import grand.app.moon.presentation.base.utils.showNoApiErrorAlert
import grand.app.moon.databinding.FragmentLogInBinding
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

      }

    }

    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_PHONE.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.required))
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
        viewModel.request.phone,
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

  private fun checkNavigate() {
    if (viewModel.registerStep == "1" || viewModel.registerStep == "2")
      navigateSafe(LogInFragmentDirections.actionLogInFragmentToCountriesFragment())
    if (viewModel.registerStep == "3" || viewModel.registerStep == "4")
      navigateSafe(LogInFragmentDirections.actionLogInFragmentToSchoolGradeFragment())
  }
}