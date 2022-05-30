package grand.app.moon.presentation.store.viewModels

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.store.adapter.StoreBlockAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.forEach
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

  val list = ObservableField(StoreListPaginateData())
  val _responseService =
    MutableStateFlow<Resource<BaseResponse<StoreListPaginateData>>>(Resource.Default)

  val response = _responseService

  val responseSubmit =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)

//  val responseSubmit = _responseSubmit

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
    list.set(data)
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
      notifyPropertyChanged(BR.page)
      notifyPropertyChanged(BR.callingService)
      show.set(true)
    }
  }


  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  val blocks = ArrayList<Int>()
  fun submit(v: View){
    Log.d(TAG, "unBlock: ${adapter.unBlocks.toString()}")
    blocks.clear()
    adapter.differ.currentList.forEach {
      if(!adapter.unBlocks.contains(it.id))
        blocks.add(it.id)
    }
    Log.d(TAG, "blocks: ${blocks.toString()}")
//    storeUseCase.unBlock(blocks)
//      .onEach {
//        responseSubmit.value = it
//      }
//      .launchIn(viewModelScope)

  }

  fun unBlock() {
    Log.d(TAG, "unBlock: ${adapter.position}")
    adapter.changeBlock(adapter.differ.currentList[adapter.position].id)
//    followStoreRequest.storeId = adapter.differ.currentList[adapter.position].id
//    storeUseCase.unBlock(followStoreRequest).launchIn(viewModelScope)
//    adapter.removeItem()
//    ListHelper.removeStoreBlock(followStoreRequest.storeId!!)
  }


}