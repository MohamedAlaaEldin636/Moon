package grand.app.moon.presentation.chat.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.presentation.notfication.adapter.NotificationAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.settings.models.NotificationData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
  private val useCase: SettingsUseCase,
) : BaseViewModel() {
  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var isLast = false

  @Bindable
  var adapter = NotificationAdapter()

  var type = 3

  val followStoreRequest = FollowStoreRequest()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<NotificationPaginateData>>>(Resource.Default)

  val response = _responseService


  fun callService() {
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if (page > 1) {
        notifyPropertyChanged(BR.page)
      }
      job = useCase.notifications(type,page)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: NotificationPaginateData) {
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

  fun reset() {
    page = 0
    isLast = false
    callingService = false
  }

  fun remove(notificationData: NotificationData) {
    job = useCase.delete(notificationData.id)
      .onEach {
      }
      .launchIn(viewModelScope)
  }

}