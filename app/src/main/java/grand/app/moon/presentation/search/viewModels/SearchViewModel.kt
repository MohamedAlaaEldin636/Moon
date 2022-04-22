package grand.app.moon.presentation.search.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.BuildConfig
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.AccountUseCases
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.use_case.AdsUseCase
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
class SearchViewModel @Inject constructor(
  val accountRepository: AccountRepository,
  val userLocalUseCase: UserLocalUseCase,
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

  var ADS_API = BuildConfig.API_BASE_URL + "v1/advertisements?"
  var ADS_LIST_URL = ""
  var tmp = ""
  val myMap = mutableMapOf(
    Constants.SEARCH to "",
    Constants.CATEGORY_ID to "",
    Constants.SUB_CATEGORY_ID to ""
  )


  val _responseService =
    MutableStateFlow<Resource<BaseResponse<AdsListPaginateData>>>(Resource.Default)

  val noData = ObservableBoolean(false)
  val isStart = ObservableBoolean(true)

  val localSearchArrayList = ArrayList<String>()
  var localSearches = ""
  val response = _responseService
  val listSearches = ArrayList<String>()
  private val TAG = "SearchViewModel"

  init {
    localSearches = accountRepository.getKeyFromLocal(Constants.SEARCH)
    if (localSearches.isNotEmpty()) {

      localSearches.split("||").toTypedArray().forEach {
        Log.d(TAG, ": $it")
        if (it.trim().isNotEmpty() && !localSearchArrayList.contains(it.trim())) localSearchArrayList.add(it)
      }
    }
    Log.d(TAG, ": ${listSearches.size}")
    adapter.percentageAds = 100
    adapter.type = 5
  }

  fun callService() {
    isStart.set(false)
    myMap.forEach {
      if (it.value.isNotEmpty())
        tmp += "${it.key}=${it.value}&"
    }
    if (myMap[Constants.SEARCH]?.trim()?.isNotEmpty() == true && !localSearchArrayList.contains(myMap[Constants.SEARCH])) {
      if (localSearches.isEmpty() )
        accountRepository.saveKeyToLocal(Constants.SEARCH, myMap[Constants.SEARCH].toString())
      else
        accountRepository.saveKeyToLocal(
          Constants.SEARCH,
          localSearches + "||" + myMap[Constants.SEARCH].toString()
        )
    }
    ADS_LIST_URL = ADS_API + tmp
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


  private fun getAdsList() {
    job = useCase.getAdsList(ADS_LIST_URL)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }


  fun setData(data: AdsListPaginateData?) {
    data?.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(it.list)
        show.set(true)
        notifyPropertyChanged(BR.adapter)
      } else {
        adapter.insertData(it.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
      noData.set(data.list.size == 0 && page == 1)
      Log.d(TAG, "ads_size: ${adapter.differ.currentList.size}")

      Log.d(TAG, "page: $page")
      Log.d(TAG, "setData: ${noData.get()}")
    }
  }


  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun reset() {
    callingService = false
    isLast = false
    page = 0
  }
}