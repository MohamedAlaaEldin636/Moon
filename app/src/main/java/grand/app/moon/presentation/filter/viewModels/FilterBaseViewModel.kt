package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.currentLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.filter.entitiy.Children
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.filter.adapter.FilterAdapter
import grand.app.moon.presentation.filter.adapter.FilterRadioSelectAdapter
import grand.app.moon.presentation.filter.adapter.FilterRateAdapter
import kotlinx.coroutines.flow.collect
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
  val adapterSort = FilterRadioSelectAdapter()
  val adapterAdsType = FilterRadioSelectAdapter()
  val rateAdapter = FilterRateAdapter()


  fun addCities(callBack: (result: FilterProperty) -> Unit) {
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

  fun addRates(callBack: (result: FilterProperty) -> Unit) {
    Log.d(TAG, "addRates: ")
    val property = FilterProperty()
    property.filterType = FILTER_TYPE.RATE
    property.name = MyApplication.instance.resources.getString(R.string.filter_by_rate)

    var list = arrayListOf<FilterProperty>()
    Log.d(TAG, "addRates: ${list.size}")
    for (i in 1..5) {
      Log.d(TAG, "addRates: $i")
      list.add(FilterProperty(id = i))
      property.children.add(Children(id = i, name = ""))
    }
    callBack(property)
  }

  fun addPriceText(callBack: (result: FilterProperty) -> Unit) {
    val property = FilterProperty()
    property.type = 1
    
    Log.d(TAG, "addPriceText: ${MyApplication.instance.currentLocale.language}")
    property.filterType = FILTER_TYPE.PRICE_TEXT
    property.name = MyApplication.instance.getString(R.string.filter_by_price)
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


  fun addSortBy(callBack: (result: FilterProperty) -> Unit) {
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

  fun addAnotherOptions(callBack: (result: FilterProperty) -> Unit) {
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.OTHER_OPTIONS
    property.name = MyApplication.instance.getString(R.string.other_options)
    property.selectedList.add(1)
    property.selectedText = MyApplication.instance.resources.getString(R.string.all_ads)

    ///1: all , premium: 2 , free: 3
    property.children.add(
      Children(
        id = 1,
        name = MyApplication.instance.getString(R.string.all_ads)
      )
    )
    property.children.add(
      Children(
        id = 2,
        name = MyApplication.instance.getString(R.string.premium_ads)
      )
    )
    property.children.add(
      Children(
        id = 3,
        name = MyApplication.instance.getString(R.string.free_ads)
      )
    )
    callBack(property)
  }

  fun addCategories(callBack: (result: FilterProperty) -> Unit) {
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

  fun addSubCategories(categoryId: Int? = 0, callBack: (result: FilterProperty) -> Unit) {
    Log.d(TAG, "getSubCategories: $categoryId")
    val property = FilterProperty()
    property.type = 1
    property.filterType = FILTER_TYPE.SUB_CATEGORY
    property.name = MyApplication.instance.resources.getString(R.string.sub_section)

    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.forEach { category ->
          categoryId?.let { categorySelected ->
            if (category.id == categorySelected) {
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
    request.max_rate = 5
  }

  protected fun resetSelected() {
    request.min_price = null
    request.max_price = null
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
        when(property.filterType){
          FILTER_TYPE.PRICE_TEXT -> {
            if (property.from?.isNotEmpty() == true) request.min_price = property.from
            if (property.to?.isNotEmpty() == true) request.max_price = property.to
          }
        }
        if (property.selectedList.isNotEmpty() && index != 0 && index != adapter.differ.currentList.size - 1) {
          Log.d(TAG, "filterSubmit: ${property.id}")
          request.properties!!.add(property)
        }
      }
    }
    if (adapterSort.lastSelected != -1) request.order_by = adapterSort.lastSelected
    if (adapterAdsType.lastSelected != -1) request.other_options = adapterAdsType.lastSelected
  }

  fun updateCallBack(filterProperty: FilterProperty) {
    when (filterProperty.filterType) {
      FILTER_TYPE.OTHER_OPTIONS -> request.other_options = filterProperty.selectedList[0]
      FILTER_TYPE.SORT_BY -> request.order_by = filterProperty.selectedList[0]
//      FILTER_TYPE.PRICE -> {
//        if (filterProperty.from?.trim()?.isNotEmpty() == true) request.min_price =
//          filterProperty.from else request.min_price = null
//        if (filterProperty.to?.trim()?.isNotEmpty() == true) request.max_price =
//          filterProperty.to else request.max_price = null
//      }
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

  fun addStaticData() {

    adapterSort.lastPosition = 0
    adapterSort.lastSelected = 1
    addSortBy {
      adapterSort.differ.submitList(it.children)
    }
    adapterAdsType.lastPosition = 0
    adapterAdsType.lastSelected = 1
    addAnotherOptions {
      adapterAdsType.differ.submitList(it.children)
    }
//    rateAdapter.lastPosition = 0
//    rateAdapter.lastSelected = 1
    addRates {
      rateAdapter.differ.submitList(it.children)
    }
  }

}