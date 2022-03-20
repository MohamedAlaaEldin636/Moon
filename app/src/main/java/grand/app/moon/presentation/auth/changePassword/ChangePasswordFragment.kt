package grand.app.moon.presentation.auth.changePassword

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.domain.auth.enums.AuthFieldsValidation
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.backToPreviousScreen
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.utils.showNoApiErrorAlert
import com.structure.base_mvvm.R
import com.structure.base_mvvm.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

  private val viewModel: ChangePasswordViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_change_password

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) { backToPreviousScreen() }
    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_PASSWORD.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.empty_password))
        }
        AuthFieldsValidation.EMPTY_CONFIRM_PASSWORD.value -> {
          showNoApiErrorAlert(
            requireActivity(),
            resources.getString(R.string.password_hint_confirm)
          )
        }
        AuthFieldsValidation.PASSWORD_NOT_MATCH.value -> {
          showNoApiErrorAlert(
            requireActivity(),
            resources.getString(R.string.not_match_password)
          )
        }
      }
    }
    lifecycleScope.launchWhenResumed {
      viewModel.changePasswordResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            backToPreviousScreen()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it, retryAction = { viewModel.changePassword() })
          }
          Resource.Default -> {}
        }
      }
    }
  }


}