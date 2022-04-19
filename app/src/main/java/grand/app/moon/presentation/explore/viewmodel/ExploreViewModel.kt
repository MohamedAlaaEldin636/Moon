package grand.app.moon.presentation.explore.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.presentation.explore.adapter.ExploreAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
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
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if(page > 1){
        notifyPropertyChanged(BR.page)
      }
      job = useCase.explore(page)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  var lastData = ExploreListPaginateData()

  private val TAG = "PackagesViewModel"

  fun setData(data: ExploreListPaginateData?) {
    data?.let {
      this.lastData= it
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