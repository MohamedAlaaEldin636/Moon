package grand.app.moon.presentation.ads.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.BuildConfig
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AdsListViewModel @Inject constructor(
  private val useCase: AdsUseCase,
) : BaseViewModel() {
  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var type: Int = -1

  var isLast = false

  @Bindable
  var adapter = AdsAdapter()

  var ADS_LIST_URL = BuildConfig.API_BASE_URL + "v1/advertisements?"

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<AdsListPaginateData>>>(Resource.Default)

  val response = _responseService

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
      if(type == -1) getAdsProfile()
      else{
        getAdsList()
      }
    }
  }

  private fun getAdsProfile() {
    job = useCase.getProfileAdsList(page, type)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }

  private fun getAdsList() {
    job = useCase.getAdsList(ADS_LIST_URL)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: AdsListPaginateData?) {
    data?.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
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