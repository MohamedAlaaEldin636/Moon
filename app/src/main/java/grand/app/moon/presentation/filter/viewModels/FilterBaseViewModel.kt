package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
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
open class FilterBaseViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
) : BaseViewModel() {
  private val TAG = "FilterBaseViewModel"
  val request = FilterResultRequest()
  var rateSelected = BooleanArray(7)
  val adapter = FilterAdapter()



  fun addCities(callBack : (result: FilterProperty) -> Unit){
    val property = FilterProperty()
    viewModelScope.launch {
      accountRepository.getCountries().collect {
        property.type = 1
        property.filterType = FILTER_TYPE.CITY
        property.name = MyApplication.instance.resources.getString(R.string.city)

        it.data.forEach { country ->
          val countryId = accountRepository.getKeyFromLocal(Constants.COUNTRY_ID)
          if (country.id.toString() == countryId) {
            country.cities.forEach {
              property.children.add(Children(it.id, name = it.name, text = it.name))
            }
          }
        }
      }
    }
    callBack(property)
  }

  fun addPrice(callBack : (result: FilterProperty) -> Unit) {
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.PRICE
    property.name = MyApplication.instance.resources.getString(R.string.filter_by_price)
    property.selectedList.add(1)
    property.selectedText = MyApplication.instance.resources.getString(R.string.most_compatible)

    ///1: all , premium: 2 , free: 3
    property.children.add(
      Children(
        id = 1,
        name = MyApplication.instance.resources.getString(R.string.most_compatible)
      )
    )
    property.children.add(
      Children(
        id = 2,
        name = MyApplication.instance.resources.getString(R.string.the_most_recent)
      )
    )
    property.children.add(
      Children(
        id = 3,
        name = MyApplication.instance.resources.getString(R.string.lowest_price)
      )
    )
    property.children.add(
      Children(
        id = 4,
        name = MyApplication.instance.resources.getString(R.string.highest_price)
      )
    )
    callBack(property)
  }


  fun addSortBy(callBack : (result: FilterProperty) -> Unit) {
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.SORT_BY
    property.name = MyApplication.instance.resources.getString(R.string.sort_by)
    property.selectedList.add(1)
    property.selectedText = MyApplication.instance.resources.getString(R.string.most_compatible)

    ///1: all , premium: 2 , free: 3
    property.children.add(
      Children(
        id = 1,
        name = MyApplication.instance.resources.getString(R.string.most_compatible)
      )
    )
    property.children.add(
      Children(
        id = 2,
        name = MyApplication.instance.resources.getString(R.string.the_most_recent)
      )
    )
    property.children.add(
      Children(
        id = 3,
        name = MyApplication.instance.resources.getString(R.string.lowest_price)
      )
    )
    property.children.add(
      Children(
        id = 4,
        name = MyApplication.instance.resources.getString(R.string.highest_price)
      )
    )
    callBack(property)
  }

  fun addAnotherOptions(callBack : (result: FilterProperty) -> Unit) {
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
    callBack(property)
  }

  fun addCategories(callBack: (result: FilterProperty) -> Unit){
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.CATEGORY
    property.name = MyApplication.instance.resources.getString(R.string.main_section)

    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.forEach { category ->
          property.children.add(
            Children(
              id = category.id!!,
              name = category.name
            )
          )
        }
        callBack(property)
      }
    }
  }

  fun addSubCategories(categoryId : Int? = 0, callBack: (result: FilterProperty) -> Unit){
    Log.d(TAG, "getSubCategories: $categoryId")
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.SUB_CATEGORY
    property.name = MyApplication.instance.resources.getString(R.string.sub_section)

    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.forEach { category ->
          categoryId?.let { categorySelected ->
            if(category.id == categorySelected){
              category.subCategories?.forEach { subCategory ->
                property.children.add(
                  Children(
                    id = subCategory.id!!,
                    name = subCategory.name
                  )
                )
              }
            }
          }//end if found CategoryId

        }
        callBack(property)
      }
    }
  }

  fun changeRate(stars: Int) {
    request.min_rate = stars
  }

  fun prepareRequest() {
    request.properties?.clear()
    if (adapter.differ.currentList.isNotEmpty()) {
      /*
      remove (cities - other_options) from properties
      index 0 : cities
      index size-1: other_options
       */
      request.properties = arrayListOf()
      adapter.differ.currentList.forEachIndexed { index, property ->
        if (property.selectedList.isNotEmpty() && index != 0 && index != adapter.differ.currentList.size - 1) {
          Log.d(TAG, "filterSubmit: ${property.id}")
          request.properties!!.add(property)
        }
      }
    }
  }

  fun updateCallBack(filterProperty: FilterProperty) {
    when (filterProperty.filterType) {
      FILTER_TYPE.OTHER_OPTIONS -> request.other_options = filterProperty.selectedList[0]
      FILTER_TYPE.SORT_BY -> request.order_by = filterProperty.selectedList[0]
      FILTER_TYPE.PRICE -> {
        if ( filterProperty.from?.trim()?.isNotEmpty() == true) request.min_price = filterProperty.from else request.min_price = null
        if ( filterProperty.to?.trim()?.isNotEmpty() == true) request.max_price = filterProperty.to else request.max_price = null
      }
      FILTER_TYPE.CATEGORY -> {
        request.categoryId = filterProperty.selectedList[0]
        addSubCategories(request.categoryId) {
          adapter.replaceSubCategories(it)
        }
      }
      FILTER_TYPE.SUB_CATEGORY -> {
        request.sub_category_id = filterProperty.selectedList[0]
      }
      FILTER_TYPE.CITY -> {
        request.cityIds = arrayListOf()
        request.cityIds!!.addAll(filterProperty.selectedList)
      }
    }
    adapter.updateFilterSelected(filterProperty)
  }


}