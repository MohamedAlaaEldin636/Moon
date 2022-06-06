package grand.app.moon.presentation.splash

import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.general.use_case.GeneralUseCases
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  val homeUseCase: HomeUseCase,
  val accountRepository: AccountRepository,
  val countriesUseCase: CountriesUseCase
) :
  BaseViewModel() {

  var isCategories: Boolean = false
  var isCountries: Boolean = false
  var lang = ""

  private val _categoryItemResponse =
    MutableStateFlow<Resource<BaseResponse<ArrayList<CategoryItem>>>>(Resource.Default)
  val categoryItemResponse = _categoryItemResponse

  val _countriesResponse =
    MutableStateFlow<Resource<BaseResponse<List<Country>>>>(Resource.Default)

  fun home() {
    homeUseCase.getCategories().onEach {
      _categoryItemResponse.value = it
    }.launchIn(viewModelScope)

    countriesUseCase.invoke()
      .onEach { result ->
        _countriesResponse.value = result
      }
      .launchIn(viewModelScope)

  }

  fun saveCategories(data: BaseResponse<ArrayList<CategoryItem>>) {
    viewModelScope.launch {
      for (category in data.data)
      Log.d(TAG, "saveCategories: ${category.name}")
      accountRepository.saveCategories(data)
    }
  }

  private val TAG = "SplashViewModel"

  fun redirect() {
    if(isCategories && isCountries) {
      clickEvent.value = when (accountRepository.getKeyFromLocal(Constants.INTRO)) {
        "true" -> Constants.HOME
        else -> {
          Constants.FIRST_TIME
        }
      }
    }
  }

  fun saveCountries(value: BaseResponse<List<Country>>) {
    viewModelScope.launch {
      accountRepository.saveCountries(value)
    }
  }

}