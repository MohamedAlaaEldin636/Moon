package com.structure.base_mvvm.presentation.auth.countries

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.auth.countries.viewModels.CountriesViewModel
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.presentation.base.extensions.handleApiError
import com.structure.base_mvvm.presentation.base.extensions.hideKeyboard
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
    viewModel.clickEvent.observe(this) {
      if (it == Constants.BACK)
        backToPreviousScreen()
      else if (it == Constants.EDUCATIONAL_STAGES && viewModel.adapter.lastSelected != -1)
        openEducationalStages()
    }
    lifecycleScope.launchWhenResumed {
      viewModel.countriesPasswordResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.updateAdapter(it.value.data)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    viewModel.adapter.changeEvent.observeForever { country ->
      binding.tvSelectedCountry.text = country.name
      viewModel.saveCountry(country.id.toString())
    }
  }

  private fun openEducationalStages() {
    navigateSafe(CountriesFragmentDirections.actionCountriesFragmentToSchoolGradeFragment())
  }
}