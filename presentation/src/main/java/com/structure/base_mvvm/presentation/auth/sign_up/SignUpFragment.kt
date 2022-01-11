package com.structure.base_mvvm.presentation.auth.sign_up

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.base.utils.getDeviceId
import com.structure.base_mvvm.presentation.base.utils.showNoApiErrorAlert
import com.structure.base_mvvm.presentation.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

  private val viewModel: SignUpViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_sign_up

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.registerRequest.device_token = getDeviceId(requireActivity())
    binding.tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        if (tab.position == 0) {
          viewModel.registerRequest.account_type = Constants.STUDENT_TYPE
          binding.inputNameTeacher.hide()
          binding.userImg.setImageResource(R.drawable.ic_user_image_holder)
        } else {
          viewModel.registerRequest.account_type = Constants.TEACHER_TYPE
          binding.inputNameTeacher.show()
          binding.userImg.setImageResource(R.drawable.ic_teacher_holder)
        }
      }

      override fun onTabUnselected(tab: TabLayout.Tab) {

      }

      override fun onTabReselected(tab: TabLayout.Tab) {

      }
    })
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) {
      when (it) {
        Constants.CONFIRM_CODE -> openConfirmCode()
        Constants.BACK -> backToPreviousScreen()
        Constants.PICK_IMAGE -> singleTedBottomPicker(requireActivity())
      }
    }
    selectedImages.observeForever { result ->
      result.path?.let { path ->
        viewModel.registerRequest.setImage(path, Constants.IMAGE)
        binding.userImg.setImageURI(result)
      }
    }
    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_NAME.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.register_name))
        }
        AuthFieldsValidation.EMPTY_NICK_NAME.value -> {
          showNoApiErrorAlert(
            requireActivity(),
            resources.getString(R.string.register_name_teacher)
          )
        }
        AuthFieldsValidation.EMPTY_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.empty_email))
        }
        AuthFieldsValidation.INVALID_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.invalid_email))
        }
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
        AuthFieldsValidation.EMPTY_TERMS.value -> {
          showNoApiErrorAlert(
            requireActivity(),
            resources.getString(R.string.empty_terms)
          )
        }
        AuthFieldsValidation.EMPTY_IMAGE.value -> {
          showNoApiErrorAlert(
            requireActivity(),
            resources.getString(R.string.empty_image)
          )
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
            openConfirmCode()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it, retryAction = { viewModel.register() })
          }
          Resource.Default -> {}
        }
      }
    }
  }

  private fun openConfirmCode() {
    navigateSafe(
      SignUpFragmentDirections.actionOpenConfirmCodeFragment(
        viewModel.registerRequest.email,
        Constants.Verify
      )
    )
  }
}