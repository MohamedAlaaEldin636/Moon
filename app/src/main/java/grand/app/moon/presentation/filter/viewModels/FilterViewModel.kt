package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.filter.FILTER_TYPE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
  private val useCase: AdsUseCase

) : FilterBaseViewModel(accountRepository) {


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
    addPriceText {
      list.add(it)
    }
//    addSortBy{
//      list.add(it)
//    }
//    addAnotherOptions{
//      list.add(it)
//    }
//    addRates{
//      rates.add(it)
//    }
    addStaticData()
    adapter.differ.submitList(list)
//    rateAdapter.differ.submitList(rates[0].children)
  }


  fun filterSubmit(v: View) {
    if (rateAdapter.lastSelected != -1) {
      request.min_rate = rateAdapter.lastSelected
      request.max_rate = 5
    }
    prepareRequest()
    toFilterResult(v,request)
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
      adapter.replaceChildren(it,FILTER_TYPE.SUB_CATEGORY)
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
    if(store.category?.size.orZero() > 0) {
      addCategoriesByStore(store.category ?: arrayListOf()) {
        Log.d(TAG, "setStore_children: ${it.children.size}")
        adapter.update(0, it)
      }
    }
  }


}