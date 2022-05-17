package grand.app.moon.presentation.subCategory.viewModel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.BuildConfig
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.subCategory.PropertiesAdapter
import grand.app.moon.presentation.subCategory.SubCategoryFragmentDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
  private val useCase: AdsUseCase,
  private val adsRepository: AdsRepository,
  private val accountRepository: AccountRepository,

  ) : BaseViewModel() {
  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var subCategoryId: Int = -1
  var categoryId: Int = -1

  var isLast = false

  val propertiesAdapter = PropertiesAdapter()
  val subCategoryResponse = ObservableField<SubCategoryResponse>()


  val isSub = ObservableBoolean(false)
  val gridOne = ObservableBoolean(true)
  var propertyId = ""
  var search = ""
  var sortBy = 1
  var type: Int = -1
  var categoryName:String? = null
  var subCategoryName:String? = null

  var adapter: AdsAdapter = AdsAdapter(adsRepository)

  var noData = ObservableBoolean(false)
  var ADS_LIST_URL = ""

  val _responseIsSubService =
    MutableStateFlow<Resource<BaseResponse<SubCategoryResponse>>>(Resource.Default)

  val _responseListAds =
    MutableStateFlow<Resource<BaseResponse<AdsListPaginateData>>>(Resource.Default)


  val response = _responseIsSubService

  init {
    adapter.percentageAds = 100
  }

  fun callService() {
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if (page > 1) {
        notifyPropertyChanged(BR.page)
      }
      getAdsList()
    }
  }

  fun reset() {
    page = 0
    callingService = false
    isLast = false
  }

  private fun getAdsList() {
    ADS_LIST_URL = "${BuildConfig.API_BASE_URL}v1/advertisements?page=${page}&order_by=$sortBy"
    if (categoryId != -1) ADS_LIST_URL += "&category_id=${categoryId}"
    if (subCategoryId != -1) ADS_LIST_URL += "&sub_category_id=${subCategoryId}"
    if (search.trim().isNotEmpty()) ADS_LIST_URL += "&search=$search"
    if (propertyId.trim().isNotEmpty()) ADS_LIST_URL += "&property_id=$propertyId"

    if (type != -1) {
      ADS_LIST_URL += "&type=$type"
      job = useCase.getAdsList(ADS_LIST_URL)
        .onEach {
          _responseListAds.value = it
        }
        .launchIn(viewModelScope)

    } else {
      job = useCase.getAdsSubCategory(ADS_LIST_URL)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  fun filter(v: View) {
    when {
      categoryId == -1 -> {
        v.findNavController().navigate(
          SubCategoryFragmentDirections.actionNavCategoryListAdsToFilterHomeFragment2()
        )
      }
      else -> {
        toFilter(
          v,
          categoryId,
          categoryName,
          subCategoryId,
          subCategoryName,
          allow_change_category = false
        )
      }
    }
  }

  fun filterSort(v: View) {
    v.findNavController().navigate(
      SubCategoryFragmentDirections.actionNavCategoryListAdsToFilterSortDialog(
        sortBy,
        FilterDialog.ADVERTISEMENT
      )
    )
  }

  fun map(v: View) {
    this.subCategoryResponse.get()?.let {
      val action =
        SubCategoryFragmentDirections.actionNavCategoryListAdsToMapnavCategoryListAds(it)
      action.subCategory = subCategoryId
      v.findNavController().navigate(action)
    }
  }

  fun propertySelect() {
    propertiesAdapter.submitSelect()
    reset()
    callService()
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: SubCategoryResponse) {
    this.subCategoryResponse.set(data)
    if (data.advertisements.list.size == 0 && page == 1) noData.set(true)
    data.let {
      println("size:" + data.advertisements.list.size)
      isLast = data.advertisements.links.next == null
      if (page == 1) {
//        adapter = InvoicesAdapter()
        propertiesAdapter.differ.submitList(it.properties)
        adapter.differ.submitList(it.advertisements.list)
        show.set(true)
        notifyPropertyChanged(BR.adapter)
      } else {
        adapter.insertData(it.advertisements.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
    }
  }


  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun setCategoryId() {
//    viewModelScope.launch {
//      accountRepository.getCategories().collect {
//        it.data.forEach { categoryItem ->
//          categoryItem.subCategories?.forEach { subCategory ->
//            if (subCategory.id == subCategoryId) {
//              categoryId = categoryItem.id!!
//              return@forEach
//            }
//          }
//        }
//      }
//    }

    Log.d(TAG, "setCategoryId: HERE")
    Log.d(TAG, "setCategoryId: $categoryId")
    Log.d(TAG, "setCategoryId: $subCategoryId")
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.forEach { category ->
          if(categoryId != -1) {
            if (category.id == categoryId) {
              categoryName = category.name
            }
          }else{
            categoryId = category.id!!
            Log.d(TAG, "setCategoryId: ${categoryId}")
            Log.d(TAG, "subCategories: ${subCategoryId}")
            category.subCategories?.forEach {
              if(it.id == subCategoryId){
                categoryName = category.name
                subCategoryName = it.name
                Log.d(TAG, "categoryName: ${categoryName}")
                Log.d(TAG, "subCategoryName: ${subCategoryName}")

              }
            }
          }
        }
      }
    }
  }

  fun changeGrid() {
    if (adapter.grid == Constants.GRID_2) {
      adapter.grid = Constants.GRID_1
      clickEvent.value = Constants.GRID_1
    } else {
      adapter.grid = Constants.GRID_2
      clickEvent.value = Constants.GRID_2
    }
  }

}