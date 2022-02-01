package app.grand.tafwak.presentation.auth.countries

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.auth.countries.viewModels.CountriesViewModel
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.backToPreviousScreen
import app.grand.tafwak.presentation.base.extensions.handleApiError
import app.grand.tafwak.presentation.base.extensions.hideKeyboard
import app.grand.tafwak.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.databinding.FragmentCountriesBinding
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
     lifecycleScope.launchWhenResumed {
      viewModel.registerResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            openEducationalStages()
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
    }
  }

  private fun openEducationalStages() {
    navigateSafe(CountriesFragmentDirections.actionCountriesFragmentToSchoolGradeFragment())
  }
}