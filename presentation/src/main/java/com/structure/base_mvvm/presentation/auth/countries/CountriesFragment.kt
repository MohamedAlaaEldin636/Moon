package com.structure.base_mvvm.presentation.auth.countries

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.presentation.base.extensions.showSnackBar
import com.structure.base_mvvm.presentation.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountriesFragment : BaseFragment<FragmentCountriesBinding>() {

  private val viewModel: CountriesViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_countries

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    binding.includedToolbar.tvTitle.text = getString(R.string.sign_up)
  }

  override
  fun setupObservers() {
    viewModel.backToPreviousScreen.observe(this) { backToPreviousScreen() }
    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_EMAIL.value -> {
          requireView().showSnackBar(resources.getString(R.string.empty_email))
        }
        AuthFieldsValidation.INVALID_EMAIL.value -> {
          requireView().showSnackBar(resources.getString(R.string.invalid_email))
        }
      }
    }

  }
}