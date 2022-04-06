package grand.app.moon.presentation.explore.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.presentation.notification.adapter.ExploreAdapter
import com.structure.base_mvvm.presentation.notification.adapter.NotificationAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.explorer.entity.ExploreListPaginateData
import grand.app.moon.domain.explorer.use_case.ExploreUseCase
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ExploreListViewModel @Inject constructor(
  private val useCase: ExploreUseCase,
) : BaseViewModel() {
  @Bindable
  var page: Int = 0
  @Bindable
  var callingService = false

  var type:Int = -1

  var isLast = false

  @Bindable
  var adapter = ExploreAdapter()


  val _responseService =
    MutableStateFlow<Resource<BaseResponse<ExploreListPaginateData>>>(Resource.Default)

  val response = _responseService

  init {

  }

  fun callService(){
    job = useCase.explore()
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: ExploreListPaginateData?) {
    data?.let {
      println("size:" + data.list.size)
      isLast = data.links.next == null
      if (page == 1) {
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(it.list)
      } else {
        adapter.insertData(it.list)
      }
      notifyPropertyChanged(BR.adapter)
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