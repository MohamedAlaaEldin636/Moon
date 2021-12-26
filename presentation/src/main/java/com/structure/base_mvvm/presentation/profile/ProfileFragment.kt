package com.structure.base_mvvm.presentation.profile

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentProfileBinding
import com.structure.base_mvvm.presentation.databinding.FragmentSignUpBinding
import com.structure.base_mvvm.presentation.profile.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

  private val viewModel: ProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_profile

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
//    navigateSafe(SignUpFragmentDirections.actionOpenConfirmCodeFragment())
  }
}