package grand.app.moon.presentation.storeFilter.viewModels

import android.util.Log
import android.view.View
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
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.auth.countries.CountriesFragmentArgs
import grand.app.moon.presentation.auth.countries.adapters.CityAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.category.adapter.CategoriesFilterAdapter
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.filter.viewModels.FilterBaseViewModel
import grand.app.moon.presentation.storeFilter.views.StoreFilterFragmentArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreFilterViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
  private val useCase: AdsUseCase
) :
  FilterBaseViewModel(accountRepository) {
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


  val _responseService =
    MutableStateFlow<Resource<BaseResponse<FilterDetails>>>(Resource.Default)
  val response = _responseService


  private val TAG = "FilterHomeViewModel"

  val list = ArrayList<FilterProperty>()

  init {
    addCategories {
      list.add(it)
    }
    addSubCategories {
      list.add(it)
    }
    addCities {
      list.add(it)
    }
    addAreas(-1) {
      list.add(it)
    }
//    addPriceText {
//      list.add(it)
//    }
//    addSortBy{
//      list.add(it)
//    }
//    addAnotherOptions{
//      list.add(it)
//    }
//    addRates{
//      rates.add(it)
//    }
    addStaticDataStore()
    adapter.differ.submitList(list)
//    rateAdapter.differ.submitList(rates[0].children)
  }


  fun filterSubmit(v: View) {
    if (rateAdapter.lastSelected != -1) {
      request.min_rate = rateAdapter.lastSelected
      request.max_rate = 5
    }
    prepareRequest()
    clickEvent.value = Constants.CONFIRM
  }


  fun callService() {
    resetSelected()
    job = useCase.filterDetails(request.categoryId!!, request.sub_category_id)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }


  fun submit(filterProperty: FilterProperty) {
    if (filterProperty.filterType == FILTER_TYPE.CATEGORY) {
      request.sub_category_id = null
      if (request.categoryId != null) {
        callService()
        addSubCategories(request.categoryId) {
          adapter.update(1,it)
        }
      }
    }
  }

  var sizeBefore = 0
  fun setData(response: FilterDetails) {

    response.filterProperties.forEach {
      when (it.type) {
        3 -> it.filterType = FILTER_TYPE.PRICE
        else -> it.filterType = FILTER_TYPE.MULTI_SELECT
      }
    }
    if(request.categoryName != null){
      adapter.setName(0,request.categoryName!!)
    }
    if(request.subCategoryName != null){
      adapter.setName(1,request.subCategoryName!!)
    }
    adapter.addAll(4, sizeBefore, response.filterProperties)
    sizeBefore = response.filterProperties.size
  }

  fun setCategoryId(int: Int,name: String) {
    request.categoryId = int
    request.categoryName = name
    addSubCategories(request.categoryId) {
      adapter.replaceChildren(it, FILTER_TYPE.SUB_CATEGORY)
    }
    callService()
  }
  fun setSubCategoryId(int: Int,name: String) {
    request.sub_category_id = int
    if(name.isNotEmpty())
      request.subCategoryName = name

  }
  fun setStoreId(int: Int) {
    request.store_id = int
  }
  fun allowChangeCategory(check: Boolean){
    adapter.allowClickable(0,check)
  }

  fun setStore(store: Store) {
    if(store.category.size > 0) {
      addCategoriesByStore(store.category) {
        Log.d(TAG, "setStore_children: ${it.children.size}")
        adapter.update(0, it)
      }
    }
  }

}