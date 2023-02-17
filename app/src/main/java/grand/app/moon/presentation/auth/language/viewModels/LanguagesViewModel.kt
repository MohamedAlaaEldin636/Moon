package grand.app.moon.presentation.auth.language.viewModels

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.core.extenstions.setInitialAppLaunch
import grand.app.moon.core.extenstions.setSelectedLangBefore
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.indexOfFirstOrNull
import grand.app.moon.extensions.navigateSafely
import grand.app.moon.presentation.auth.countries.adapters.CountriesAdapter
import grand.app.moon.presentation.auth.language.LanguageFragment
import grand.app.moon.presentation.auth.language.LanguageFragmentArgs
import grand.app.moon.presentation.auth.language.LanguageFragmentDirections
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.splash.SplashActivity
import grand.app.moon.presentation.splash.isSameAsCurrentLocale
import grand.app.moon.presentation.splash.setCurrentLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
  val accountRepository: AccountRepository

) : BaseViewModel() {

  val languages = arrayListOf<Country>()
  @Bindable
  val adapter: CountriesAdapter = CountriesAdapter()
  @Bindable
  var lang : String = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

	var oldSetLang : String = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

  var languageNavArgs: LanguageFragmentArgs? = null
  private  val TAG = "LanguagesViewModel"
  init {
    Log.d(TAG, "language Before : $lang")
    if(lang.isEmpty()) lang = Constants.DEFAULT_LANGUAGE
    Log.d(TAG, "language $lang ")
    languages.add(Country(name = "English" ,lang = "en",active = if(lang == "en" ) 1 else 0))
    languages.add(Country(name = "عربى" ,lang = "ar",active = if(lang == "ar") 1 else 0))
    Log.d(TAG, "language: "+languages[0].active)
    Log.d(TAG, "language: "+languages[1].active)
	  languages.indexOfFirstOrNull { it.active == 1 }?.also { index ->
			adapter.lastPosition = index
			adapter.lastSelected = languages[index].id
		  //this.lang = languages[index].lang
		  //oldSetLang = languages[index].lang
	  }
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

	fun updateLanguageWithoutLocalSave(lang: String) {
		this.lang = lang
		notifyPropertyChanged(BR.lang)
	}
	fun updateLanguageLocalSave(lang: String) {
		accountRepository.saveKeyToLocal(Constants.LANGUAGE,lang)
	}

  fun next(v : View) {
	  val fragment = v.findFragmentOrNull<LanguageFragment>() ?: return
	  val activity = fragment.activity ?: return

	  if (languageNavArgs?.type == Constants.SPLASH) {
		  fragment.findNavController().navigateSafely(
			  LanguageFragmentDirections.actionLanguageFragmentToCountriesFragment2()
		  )
	  }else {
		  fragment.findNavController().navigateUp()
	  }

	  if (activity.isSameAsCurrentLocale(lang).not()) {
		  activity.setCurrentLocale(lang)
	  }

    /*Log.d(TAG, "next: ")
    languageNavArgs?.type?.let {
      Log.d(TAG, "next: $it")
      if(it == Constants.SPLASH) {
	      if (this.lang == oldSetLang) {
		      v.findNavController().navigate(LanguageFragmentDirections.actionLanguageFragmentToCountriesFragment2())
	      }else {
		      accountRepository.saveKeyToLocal(Constants.LANGUAGE_SELECTED_ON_APP_LAUNCH_BEFORE, true.toString())

		      clickEvent.value = Constants.BACK
	      }
			} else {
	      clickEvent.value = Constants.BACK
      }
    }*/
  }
  fun back(v: View){
    v.findNavController().popBackStack()
  }
}