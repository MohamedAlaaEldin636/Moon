package grand.app.moon.presentation.auth.countries.viewModels

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
//import grand.app.moon.domain.countries.entity.Country
//import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
//import grand.app.moon.presentation.auth.countries.adapters.CountriesAdapter
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.presentation.auth.countries.CountriesFragmentArgs
import grand.app.moon.presentation.auth.countries.CountriesFragmentDirections
import grand.app.moon.presentation.auth.countries.adapters.CountriesAdapter
import grand.app.moon.presentation.auth.language.LanguageFragmentArgs
import grand.app.moon.presentation.auth.language.LanguageFragmentDirections
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
  private val countriesUseCase: CountriesUseCase,
  private val accountRepository: AccountRepository
) :
  BaseViewModel() {
  private val _countriesPasswordResponse =
    MutableStateFlow<Resource<BaseResponse<List<Country>>>>(Resource.Default)
  val countriesPasswordResponse = _countriesPasswordResponse
  var countriesFragmentArgs: CountriesFragmentArgs? = null

  @Bindable
  val adapter: CountriesAdapter = CountriesAdapter()

  @Bindable
  var countryId = accountRepository.getKeyFromLocal(Constants.COUNTRY_ID)

  init {
    countryId
    getCountries()
  }

  private val TAG = "CountriesViewModel"
  private fun getCountries() {
//    Log.d(TAG, "getCountries: $countryId")
    countriesUseCase.invoke()
      .onEach { result ->
        _countriesPasswordResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun back(v: View){
    v.findNavController().navigateUp()
  }

  fun updateAdapter(countriesResponse:BaseResponse<List<Country>>) {
    viewModelScope.launch {
      accountRepository.saveCountries(countriesResponse)
    }
    countriesResponse.data.forEach { it.active = 0 }
    countriesResponse.data.forEachIndexed { index, it ->
      if (countryId.isNotEmpty() && it.id == countryId.toInt()) {
        it.active = 1
        adapter.lastSelected = it.id
        adapter.lastPosition = index
      }
    }
    adapter.differ.submitList(countriesResponse.data)
    notifyPropertyChanged(BR.adapter)
  }

  fun updateCountry(countryId: String) {
    accountRepository.saveKeyToLocal(Constants.COUNTRY_ID, countryId)
    this.countryId = countryId
    notifyPropertyChanged(BR.countryId)
  }

  fun next(v: View) {
    countriesFragmentArgs?.from?.let {
      if (it == Constants.SPLASH)
        v.findNavController().navigate(CountriesFragmentDirections.actionCountriesFragment2ToIntroFragment(null,-1))
      else
        clickEvent.value = Constants.BACK
    }
  }

}