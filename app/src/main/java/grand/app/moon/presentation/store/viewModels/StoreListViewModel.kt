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
import grand.app.moon.presentation.base.utils.Constants
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

  fun changeGrid(isGrid2: Boolean) {
    Log.d(TAG, "changeGrid: ${isGrid2}")
    this.isGrid2.set(isGrid2)
    if (this.isGrid2.get()) {
      adapter.grid = Constants.GRID_2
      clickEvent.value = Constants.GRID_2
    } else {
      adapter.grid = Constants.GRID_1
      clickEvent.value = Constants.GRID_1
    }
  }

  fun addStoreNow(v: View) {

  }

  fun storeFilter(v: View) {
    v.findNavController().navigate(StoreListFragmentDirections.actionStoreListFragmentToStoreFilterFragment(request))
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
      job = storeUseCase.getStores(request)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }


  fun setData(data: StoreListPaginateData) {
    data.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
        Log.d(TAG, "setData: submitList")
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(it.list)
        notifyPropertyChanged(BR.adapter)
      } else {
        Log.d(TAG, "setData: insertData")
        adapter.insertData(it.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
      show.set(true)
    }
  }

  fun reset(){
    page = 0
    isLast = false
    callingService = false
  }

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }


}