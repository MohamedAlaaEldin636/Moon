package app.grand.tafwak.presentation.auth.changePassword

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.auth.enums.AuthFieldsValidation
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.backToPreviousScreen
import app.grand.tafwak.presentation.base.extensions.handleApiError
import app.grand.tafwak.presentation.base.extensions.hideKeyboard
import app.grand.tafwak.presentation.base.utils.showNoApiErrorAlert
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