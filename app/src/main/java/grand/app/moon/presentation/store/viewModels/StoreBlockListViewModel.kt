package grand.app.moon.presentation.store.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.presentation.notification.adapter.ExploreListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.adapter.StoreBlockAdapter
import grand.app.moon.presentation.store.adapter.StoreFollowingAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StoreBlockListViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var type: Int = -1

  var isLast = false
  var isLoggin = userLocalUseCase.isLoggin()

  @Bindable
  var adapter = StoreBlockAdapter()

  val followStoreRequest = FollowStoreRequest()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<StoreListPaginateData>>>(Resource.Default)

  val response = _responseService

  fun callService() {
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if (page > 1) {
        notifyPropertyChanged(BR.page)
      }
      job = storeUseCase.getBlockedStores(page)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: StoreListPaginateData) {
    data.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(it.list)
        notifyPropertyChanged(BR.adapter)
      } else {
        adapter.insertData(it.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
      show.set(true)
    }
  }


  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun unBlock() {
    followStoreRequest.storeId = adapter.differ.currentList[adapter.position].id
    storeUseCase.unBlock(followStoreRequest).launchIn(viewModelScope)
    adapter.removeItem()

  }


}