package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.filter.FilterFragmentDirections
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
    Log.d(TAG, ": ${list.size}")
    adapter.differ.submitList(list)
//    rateAdapter.differ.submitList(rates[0].children)
  }


  fun filterSubmit(v: View) {
    if (rateAdapter.lastSelected != -1) {
      request.min_rate = rateAdapter.lastSelected
    }
    prepareRequest()
    Log.d(TAG, "filterSubmit: ${request.min_price}")
    Log.d(TAG, "filterSubmit: ${request.max_price}")
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
    adapter.addAll(3, sizeBefore, response.filterProperties)
    sizeBefore = response.filterProperties.size
  }


}