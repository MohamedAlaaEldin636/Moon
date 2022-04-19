package grand.app.moon.presentation.auth.countries

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.auth.countries.viewModels.CountriesViewModel
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.splash.SplashActivity
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CountriesFragment : BaseFragment<FragmentCountriesBinding>() {

  private val viewModel: CountriesViewModel by viewModels()
  val countriesFragmentArgs: CountriesFragmentArgs by navArgs()

  override
  fun getLayoutId() = R.layout.fragment_countries

  override
  fun setBindingVariables() {
    viewModel.countriesFragmentArgs = countriesFragmentArgs
    binding.viewModel = viewModel
  }

  override
  fun setupObservers() {

    lifecycleScope.launchWhenResumed {
      viewModel.countriesPasswordResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.updateAdapter(it.value)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }

    viewModel.adapter.changeEvent.observe(viewLifecycleOwner, { country ->
      viewModel.updateCountry(country.id.toString())
    })


    viewModel.clickEvent.observe(viewLifecycleOwner,{
      openActivityAndClearStack(SplashActivity::class.java)
    })
  }

}