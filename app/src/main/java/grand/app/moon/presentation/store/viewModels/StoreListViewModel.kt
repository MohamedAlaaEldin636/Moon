package grand.app.moon.presentation.store.viewModels

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
import grand.app.moon.R
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreFilterRequest
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.adapter.StoreAdapter
import grand.app.moon.presentation.store.views.StoreListFragmentDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StoreListViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  private val TAG = "StoreListViewModel"

  var citiesSelected = ""
  var categoriesSelected = ""

  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var type: Int = -1

  var isLast = false
  var isLoggin = userLocalUseCase.isLoggin()

//  var request = StoreFilterRequest()
  var request = FilterResultRequest()

  @Bindable
  var adapter = StoreAdapter()

  val followStoreRequest = FollowStoreRequest()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<StoreListPaginateData>>>(Resource.Default)

  val response = _responseService

    //1 Newest, 2 Oldest, 3 Average rate

  init {
    adapter.isLogin = isLoggin
    adapter.useCase = storeUseCase
    adapter.grid = Constants.GRID_2
  }

  val gridOne = ObservableBoolean(false)

  fun changeGrid() {

    Log.d(TAG, "changeGrid: ${isGrid2}")
    if (gridOne.get()) {
      adapter.grid = Constants.GRID_2
      clickEvent.value = Constants.GRID_2
//      this.isGrid2.set(true)
      gridOne.set(false)
    } else {
      adapter.grid = Constants.GRID_1
      clickEvent.value = Constants.GRID_1
//      this.isGrid2.set(false)
      gridOne.set(true)
    }
  }


  fun storeFilter(v: View) {
//    v.findNavController()
//      .navigate(StoreListFragmentDirections.actionStoreListFragmentToStoreFilterFragment(request))

    val uri = Uri.Builder()
      .scheme("storeFilter")
      .authority("grand.app.moon.store.filter")
      .build()

    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
    v.findNavController().navigate(request)


  }

  fun callService() {
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if (page > 1) {
        notifyPropertyChanged(BR.page)
      }
      request.page = page
//      Log.d(TAG, "callService: ${request.city_ids.size}")
      job = storeUseCase.getStores(request)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  //{title}/{type}/{advertisement_id}/{store_id}
  fun filterSort(v: View){
    val uri = Uri.Builder()
      .scheme("filter-sort-report")
      .authority("grand.app.moon.filterSortDialog")
      .appendPath(request.order_by.toString())
      .appendPath(FilterDialog.STORE.toString())
      .build()
    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
    v.findNavController().navigate(request)
  }

  fun map(v: View){
    val item = SubCategoryResponse()
    val action = StoreListFragmentDirections.actionStoreListFragmentToMapFragment(item)
    action.orderBy = request.order_by
    v.findNavController().navigate(action)
//    val uri = Uri.Builder()
//      .scheme("map")
//      .authority("grand.app.moon.map")
//      .appendPath(request.orderBy.toString())
//      .build()
//    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
//    v.findNavController().navigate(request)
  }

  fun setData(data: StoreListPaginateData) {
    data.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
        Log.d(TAG, "setData: submitList")
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(null)
        adapter.differ.submitList(it.list)
      } else {
        Log.d(TAG, "setData: insertData")
        adapter.insertData(it.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
      show.set(true)
    }
    ListHelper.addFollowStore(data.list)
    notifyPropertyChanged(BR.page)
    notifyPropertyChanged(BR.adapter)
  }

  fun reset() {
    page = 0
    isLast = false
    callingService = false
  }

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun follow() {
    followStoreRequest.storeId = adapter.differ.currentList[adapter.position].id
    storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
    adapter.changeFollowingText()
  }

  fun notifyAdapters() {
    notifyPropertyChanged(BR.adapter)
  }

}