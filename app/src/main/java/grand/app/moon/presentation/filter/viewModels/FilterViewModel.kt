package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.filter.entitiy.Children
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.filter.FilterAdapter
import grand.app.moon.presentation.filter.FilterFragmentDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
  private val useCase: AdsUseCase
) : BaseViewModel() {
  val request = FilterResultRequest()
  var rateSelected = BooleanArray(7)

  private val TAG = "FilterViewModel"
  val _responseService =
    MutableStateFlow<Resource<BaseResponse<FilterDetails>>>(Resource.Default)
  val response = _responseService
  val adapter = FilterAdapter()


  fun callService() {
    job = useCase.filterDetails(request.categoryId!!, request.sub_category_id!!)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }

  fun setData(response: FilterDetails) {
    viewModelScope.launch {
      accountRepository.getCountries().collect {
        addCities(response, it)
        addAnotherOptions(response)
        Log.d(TAG, "setData Children: ${response.filterProperties.size}")
        adapter.differ.submitList(response.filterProperties)
      }
    }
  }

  private fun addAnotherOptions(response: FilterDetails) {
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.OTHER_OPTIONS
    property.name = MyApplication.instance.resources.getString(R.string.other_options)
    property.selectedList.add(1)
    property.selectedText = MyApplication.instance.resources.getString(R.string.all_ads)

    ///1: all , premium: 2 , free: 3
    property.children.add(
      Children(
        id = 1,
        name = MyApplication.instance.resources.getString(R.string.all_ads)
      )
    )
    property.children.add(
      Children(
        id = 2,
        name = MyApplication.instance.resources.getString(R.string.premium_ads)
      )
    )
    property.children.add(
      Children(
        id = 3,
        name = MyApplication.instance.resources.getString(R.string.free_ads)
      )
    )
    response.filterProperties.add(property)
  }

  private fun addCities(response: FilterDetails, it: BaseResponse<List<Country>>) {
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.CITY
    property.name = MyApplication.instance.resources.getString(R.string.city)

    it.data.forEach { country ->
      val countryId = accountRepository.getKeyFromLocal(Constants.COUNTRY_ID)
      Log.d(TAG, "getCities: each ${country.id}")
      Log.d(TAG, "setData: ${countryId}")
      if (country.id.toString() == countryId) {
        country.cities.forEach {
          property.children.add(Children(it.id, name = it.name, text = it.name))
        }
        Log.d(TAG, "setData_children: ${property.children.size}")
      }
    }
    response.filterProperties.add(0, property) //add Citites
  }

  fun changeRate(stars: Int) {
    request.min_rate = stars
  }

  fun updateCallBack(filterProperty: FilterProperty) {
    if (filterProperty.filterType == FILTER_TYPE.OTHER_OPTIONS) {
      request.other_options = filterProperty.selectedList[0]
    }
    adapter.updateFilterSelected(filterProperty)
  }

  fun filterSubmit(v: View) {
    request.properties?.clear()
    request.cityIds?.clear()
    if (adapter.differ.currentList.isNotEmpty() && adapter.differ.currentList[0].selectedList.isNotEmpty()) {
      request.cityIds?.addAll(adapter.differ.currentList[0].selectedList)
    }
    if (adapter.differ.currentList.isNotEmpty()) {
      adapter.differ.currentList.forEach {
        if (it.selectedList.isNotEmpty())
          request.properties?.add(it)
      }
    }
    v.findNavController()
      .navigate(FilterFragmentDirections.actionFilterFragmentToFilterResultsFragment(request))
  }
}