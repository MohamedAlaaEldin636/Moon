package grand.app.moon.presentation.search.viewModels

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.BuildConfig
import grand.app.moon.R
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.AccountUseCases
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.entity.SearchData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.adapter.StoreAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  val accountRepository: AccountRepository,
  val userLocalUseCase: UserLocalUseCase,
  val adsRepository: AdsRepository,
  private val useCase: AdsUseCase,
  private val storeUseCase: StoreUseCase,
) : BaseViewModel() {

  @Bindable
  val storesAdapter = StoreAdapter()

  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

//  var type: Int = 5

  var isLast = false

  var  adapter : AdsAdapter = AdsAdapter(adsRepository)

  var categoryId : Int? = null
  var subCategoryId : Int? = null
  var search = ""
  val filterRequest = FilterResultRequest()


  var ADS_API = BuildConfig.API_BASE_URL + "v1/advertisements?"
  var ADS_LIST_URL = ""
//  var tmp = ""
//  val myMap = mutableMapOf(
//    Constants.SEARCH to "",
//    Constants.CATEGORY_ID to "",
//    Constants.SUB_CATEGORY_ID to ""
//  )


  val _responseService =
    MutableStateFlow<Resource<BaseResponse<SearchData>>>(Resource.Default)

  val noData = ObservableBoolean(false)
  val isStart = ObservableBoolean(true)

  val localSearchArrayList = ArrayList<String>()
  var localSearches = ""
  val response = _responseService
  val listSearches = ArrayList<String>()
  private val TAG = "SearchViewModel"

  init {
    storesAdapter.useCase = storeUseCase
    localSearches = accountRepository.getKeyFromLocal(Constants.SEARCH)
    if (localSearches.isNotEmpty()) {

      localSearches.split("||").toTypedArray().forEach {
        Log.d(TAG, ": $it")
        if (it.trim().isNotEmpty() && !localSearchArrayList.contains(it.trim())) localSearchArrayList.add(it)
      }
    }
    Log.d(TAG, ": ${listSearches.size}")
    adapter.percentageAds = 100
    adapter.showFavourite = true
    adapter.type = 5
  }

  fun callService() {
    isStart.set(false)
    if (search.trim().isNotEmpty() && !localSearchArrayList.contains(search)) {
      if (localSearches.isEmpty() )
        accountRepository.saveKeyToLocal(Constants.SEARCH, search)
      else
        accountRepository.saveKeyToLocal(
          Constants.SEARCH,
          "$localSearches||$search"
        )
    }
//    ADS_LIST_URL = ADS_API + tmp
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      filterRequest.page = page
      if (page > 1) {
        notifyPropertyChanged(BR.page)
      }
      getAdsList()
    }
  }


  private fun getAdsList() {
//    if(ADS_LIST_URL.isNotEmpty()){
//      if(ADS_LIST_URL.substring(ADS_LIST_URL.length - 1) == "&"){
//        ADS_LIST_URL = ADS_LIST_URL.substring(0,ADS_LIST_URL.length - 1)
//      }
//    }
//    filterRequest.categoryId = categoryId
//    filterRequest.sub_category_id = subCategoryId
    filterRequest.search = search
    job = useCase.search(search,page,page == 1)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }


  fun setData(data: SearchData?) {
    data?.let {
      println("size:" + data.ads.list.size)
      isLast = it.ads.links.next == null
      if (page == 1) {
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(null)
        adapter.differ.submitList(it.ads.list)
        show.set(true)
        notifyPropertyChanged(BR.adapter)
        Log.d(TAG, "setDataStores: ${it.stores.size}")
        storesAdapter.differ.submitList(null)
        storesAdapter.differ.submitList(it.stores)
        notifyPropertyChanged(BR.storesAdapter)


      } else {
        adapter.insertData(it.ads.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
      noData.set(data.ads.list.size == 0 && page == 1 && it.stores.size == 0)
      Log.d(TAG, "ads_size: ${adapter.differ.currentList.size}")

      Log.d(TAG, "page: $page")
      Log.d(TAG, "setData: ${noData.get()}")
    }
  }

  val followStoreRequest = FollowStoreRequest()
  fun follow() {
    followStoreRequest.storeId = storesAdapter.differ.currentList[storesAdapter.position].id
    storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
    storesAdapter.changeFollowingText()
  }

  fun allStores(v: View){
    val uri = Uri.Builder()
      .scheme("storeListSearch")
      .authority("grand.app.moon.store.search.List")
      .appendPath(v.context.getString(R.string.stores))
      .appendPath(3.toString())
      .appendPath("-1")
      .appendPath(search)
      .build()
    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
    v.findNavController().navigate(request)
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