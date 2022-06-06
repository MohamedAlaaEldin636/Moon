package grand.app.moon.presentation.store.viewModels

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.structure.base_mvvm.presentation.notification.adapter.ExploreListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreFilterRequest
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.extensions.navigateSafe
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.openBrowser
import grand.app.moon.presentation.more.SettingsFragmentDirections
import grand.app.moon.presentation.store.adapter.StoreAdapter
import grand.app.moon.presentation.store.adapter.StoreFollowingAdapter
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

  var request = StoreFilterRequest()

  @Bindable
  var adapter = StoreAdapter()

  val followStoreRequest = FollowStoreRequest()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<StoreListPaginateData>>>(Resource.Default)

  val response = _responseService

  init {
    adapter.isLogin = isLoggin
    adapter.useCase = storeUseCase

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

  fun addStoreNow(v: View) {
    v.findNavController().navigate(
      StoreListFragmentDirections.actionStoreListFragmentToWebFragment2(
        v.context.getString(
          R.string.add_store_now
        ),
        "https://moontest.my-staff.net/store/register"
      )
    )
  }


  fun storeFilter(v: View) {
    v.findNavController()
      .navigate(StoreListFragmentDirections.actionStoreListFragmentToStoreFilterFragment(request))
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
      Log.d(TAG, "callService: ${request.city_ids.size}")
      job = storeUseCase.getStores(request)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  fun filterSort(v: View){

  }

  fun map(v: View){

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