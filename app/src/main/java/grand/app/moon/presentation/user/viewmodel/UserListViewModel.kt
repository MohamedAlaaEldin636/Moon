package grand.app.moon.presentation.user.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import grand.app.moon.presentation.user.adapter.UserListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val useCase: ExploreUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  @Bindable
  var page: Int = 0
  @Bindable
  var callingService = false
  var exploreId = -1

  val title = ObservableField("")

  var isLast = false

  @Bindable
  var adapter = UserListAdapter()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<UserListPaginateData>>>(Resource.Default)
  val response = _responseService


  init {

  }

  fun callService(){
    if (!callingService && !isLast) {
      Log.d(TAG, "callService WORKING: ${page}")
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if(page > 1){
        notifyPropertyChanged(BR.page)
      }
      job = useCase.getUsers(exploreId,page)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: UserListPaginateData) {
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

}