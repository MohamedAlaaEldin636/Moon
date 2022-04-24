package grand.app.moon.presentation.filter.result

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.BuildConfig
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FilterResultsViewModel @Inject constructor(
  private val useCase: AdsUseCase,
) : BaseViewModel() {
  private val TAG = "FilterResultsViewModel"

  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var type: Int = -1

  var isLast = false

  @Bindable
  var adapter = AdsAdapter()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<AdsListPaginateData>>>(Resource.Default)

  val response = _responseService
  val noData = ObservableBoolean(false)

  lateinit var request: FilterResultRequest

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
      job = useCase.filterResults(request)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  fun setData(data: AdsListPaginateData?) {
    data?.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
        Log.d(TAG, "setData: submitList")
//        adapter = InvoicesAdapter()
        when{
          it.list.size > 0 -> {
            noData.set(false)
            adapter.differ.submitList(it.list)
            notifyPropertyChanged(BR.adapter)
          }
          else -> noData.set(true)
        }
        show.set(true)
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