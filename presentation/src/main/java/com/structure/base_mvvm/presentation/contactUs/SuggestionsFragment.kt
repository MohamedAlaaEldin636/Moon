package com.structure.base_mvvm.presentation.contactUs

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.base.utils.showNoApiErrorAlert
import com.structure.base_mvvm.presentation.base.utils.showSuccessAlert
import com.structure.base_mvvm.presentation.contactUs.viewModels.ContactUsViewModel
import com.structure.base_mvvm.presentation.databinding.FragmentContactUsBinding
import com.structure.base_mvvm.presentation.databinding.FragmentSuggestionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SuggestionsFragment : BaseFragment<FragmentSuggestionsBinding>() {

  private val viewModel: ContactUsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_suggestions

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setupObservers() {

    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_NAME.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.register_name))
        }
        AuthFieldsValidation.EMPTY_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.empty_email))
        }
        AuthFieldsValidation.INVALID_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.invalid_email))
        }
        AuthFieldsValidation.EMPTY_PHONE.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.register_phone))
        }
        AuthFieldsValidation.EMPTY_CONTENT.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.message))
        }

      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel.contactResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            showSuccessAlert(requireActivity(), it.value.message)
            backToPreviousScreen()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(
              it,
              retryAction = { viewModel.onContactClicked() })
          }

        }
      }
    }
  }
}