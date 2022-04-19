package grand.app.moon.presentation.storeFilter.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import grand.app.moon.domain.countries.entity.Country
//import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
//import grand.app.moon.presentation.auth.countries.adapters.CountriesAdapter
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.presentation.auth.countries.CountriesFragmentArgs
import grand.app.moon.presentation.auth.countries.adapters.CityAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.category.adapter.CategoriesFilterAdapter
import grand.app.moon.presentation.storeFilter.views.StoreFilterFragmentArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreFilterViewModel @Inject constructor(
  private val accountRepository: AccountRepository
) :
  BaseViewModel() {
  private val _countriesPasswordResponse =
    MutableStateFlow<Resource<BaseResponse<List<Country>>>>(Resource.Default)
  val countriesPasswordResponse = _countriesPasswordResponse
  var args: StoreFilterFragmentArgs? = null

  @Bindable
  val cityAdapter: CityAdapter = CityAdapter()

  @Bindable
  val categoriesAdapter = CategoriesFilterAdapter()

  @Bindable
  var countryId = accountRepository.getKeyFromLocal(Constants.COUNTRY_ID)

  fun confirm(){
    clickEvent.value = Constants.CONFIRM
  }

  fun init(){
    args?.let {
      if(it.storeFilter.city_ids.isNotEmpty()){
        cityAdapter.selected.addAll(it.storeFilter.city_ids)
      }
    }
    getCities()

    args?.let {
      if(it.storeFilter.category_ids.isNotEmpty()){
        categoriesAdapter.selected.addAll(it.storeFilter.category_ids)
      }
    }
    getCategories()
  }

  private val TAG = "StoreFilterViewModel"
  private fun getCities() {
    Log.d(TAG, "getCities: default $countryId")
    viewModelScope.launch {
      accountRepository.getCountries().collect {
        it.data.forEach { country ->
          Log.d(TAG, "getCities: each ${country.id}")
          if(country.id.toString() == countryId) {
            cityAdapter.submitList(country.cities)
            Log.d(TAG, "getCities: ${cityAdapter.differ.currentList.size}")
            notifyPropertyChanged(BR.cityAdapter)
          }
        }
      }
    }
  }

  private fun getCategories() {
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        categoriesAdapter.differ.submitList(it.data)
        notifyPropertyChanged(BR.categoriesAdapter)
      }
    }
  }



}