package grand.app.moon.presentation.ads.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.BuildConfig
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AdsListViewModel @Inject constructor(
  val adsRepository: AdsRepository,
  private val useCase: AdsUseCase,
  private val adsUseCase: AdsUseCase
) : BaseViewModel() {
  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var type: Int? = null

  var callProfilePage = false
  var isLast = false

  @Bindable
  var adapter: AdsAdapter = AdsAdapter(adsRepository)
  val list = ObservableField(AdsListPaginateData())

  var isProfile = ObservableBoolean(false)
  var total = ObservableField("0")

  var ADS_LIST_URL = BuildConfig.API_BASE_URL + "v1/advertisements?"

  var categoryId: Int? = null
  var subCateoryId: Int? = null
  var orderBy: Int = 1
  var storeId: Int? = null


  val response_sub_category =
    MutableStateFlow<Resource<BaseResponse<SubCategoryResponse>>>(Resource.Default)

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<AdsListPaginateData>>>(Resource.Default)

  val response = _responseService

  init {
    adapter.percentageAds = 100
    adapter.showFavourite = true
  }

  fun callService() {
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      notifyPropertyChanged(BR.page)

      Log.d(TAG, "callService: $type")
      if (callProfilePage) getAdsProfile()
      else {
        getAdsList()
      }
    }
  }

  private fun getAdsProfile() {
    Log.d(TAG, "getAdsProfile: ${page} , $type")
    isProfile.set(true)
    job = useCase.getProfileAdsList(page, type)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }

  private fun getAdsList() {
    if (type != null) {
      job = useCase.getAdsList(type, categoryId, subCateoryId, orderBy, storeId, "", page)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }else if(categoryId != null){
      job = adsUseCase.getAdsSubCategory(null, categoryId, null, 1, null, "", arrayListOf(), page)
        .onEach {
          response_sub_category.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: AdsListPaginateData?) {
    list.set(data)
    data?.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
        total.set(it.list.size.toString())
        Log.d(TAG, "setData: submitList")
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(it.list)
        show.set(true)
        notifyPropertyChanged(BR.adapter)
      } else {
        Log.d(TAG, "setData: insertData")
        adapter.insertData(it.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
    }
  }


  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }
}