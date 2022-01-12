package com.structure.base_mvvm.presentation.auth.changePassword

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.presentation.base.extensions.handleApiError
import com.structure.base_mvvm.presentation.base.extensions.hideKeyboard
import com.structure.base_mvvm.presentation.base.utils.showNoApiErrorAlert
import com.structure.base_mvvm.presentation.databinding.FragmentChangePasswordBinding
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