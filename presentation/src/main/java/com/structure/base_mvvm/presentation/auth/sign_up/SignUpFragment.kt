package com.structure.base_mvvm.presentation.auth.sign_up

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

  private val viewModel: SignUpViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_sign_up

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) {
      if (it == Constants.CONFIRM_CODE)
        openConfirmCode()
    }
    viewModel.backToPreviousScreen.observe(this) { backToPreviousScreen() }
  }

  private fun openConfirmCode() {
    navigateSafe(SignUpFragmentDirections.actionOpenConfirmCodeFragment())
  }
}