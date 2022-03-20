package grand.app.moon.presentation.auth.countries

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.auth.countries.viewModels.CountriesViewModel
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.backToPreviousScreen
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.navigateSafe
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