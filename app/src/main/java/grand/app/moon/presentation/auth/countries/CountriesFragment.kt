package grand.app.moon.presentation.auth.countries

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.auth.countries.viewModels.CountriesViewModel
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.myStore.model.toResponseCountry
import grand.app.moon.presentation.splash.MASplash2Activity
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

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.accountRepository.saveKeyToLocal(Constants.LANGUAGE_SELECTED_ON_APP_LAUNCH_BEFORE, false.toString())
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
	          viewModel.repoShop.setCountriesWithCitiesWithAreas(
		          viewModel.adapter.differ.currentList.filterNotNull().map { country ->
			          country.toResponseCountry()
							}
	          )
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
	        else -> {}
        }
      }
    }

    viewModel.adapter.changeEvent.observe(viewLifecycleOwner) { country ->
	    viewModel.updateCountry(country)
    }


	  viewModel.clickEvent.observe(viewLifecycleOwner) {
		  openActivityAndClearStack(MASplash2Activity::class.java)
	  }
  }

}