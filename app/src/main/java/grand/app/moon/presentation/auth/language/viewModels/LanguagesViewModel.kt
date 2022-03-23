package grand.app.moon.presentation.auth.language.viewModels

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.presentation.auth.countries.adapters.CountriesAdapter
import grand.app.moon.presentation.auth.language.LanguageFragmentArgs
import grand.app.moon.presentation.auth.language.LanguageFragmentDirections
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
  private val accountRepository: AccountRepository
) : BaseViewModel() {

  val languages = arrayListOf<Country>()
  @Bindable
  val adapter: CountriesAdapter = CountriesAdapter()
  @Bindable
  var lang : String = accountRepository.getKeyFromLocal(Constants.LANGUAGE)
  var languageNavArgs: LanguageFragmentArgs? = null
  private  val TAG = "LanguagesViewModel"
  init {
    Log.d(TAG, "language $lang ")
    languages.add(Country(name = "عربى" ,lang = "ar",active = if(lang == "ar") 1 else 0))
    languages.add(Country(name = "English" ,lang = "en",active = if(lang == "en" ) 1 else 0))
    Log.d(TAG, "language: "+languages[0].active)
    Log.d(TAG, "language: "+languages[1].active)
    updateAdapter(languages)
  }
  fun updateAdapter(countries: List<Country>) {
    adapter.differ.submitList(countries)
    notifyPropertyChanged(BR.adapter)
  }

  fun updateLanguage(lang: String) {
    accountRepository.saveKeyToLocal(Constants.LANGUAGE,lang)
    this.lang = lang
    notifyPropertyChanged(BR.lang)
  }

  fun next(v : View){
    Log.d(TAG, "next: ")
    languageNavArgs?.type?.let {
      Log.d(TAG, "next: $it")
      if(it == Constants.SPLASH)
        v.findNavController().navigate(LanguageFragmentDirections.actionLanguageFragmentToCountriesFragment2())
      else
        clickEvent.value = Constants.BACK
    }
  }
}