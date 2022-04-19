package grand.app.moon.presentation.comment.viewmodel

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.presentation.notification.adapter.CommentListAdapter
import com.structure.base_mvvm.presentation.notification.adapter.ExploreListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val useCase: ExploreUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  @Bindable
  var page: Int = 0
  @Bindable
  var callingService = false
  val exploreAction = ExploreAction()
  var exploreId = -1
  var type:Int = -1

  val total = ObservableField<String>("")

  var isLast = false
  var isLoggin = userLocalUseCase.isLoggin()
  var user = userLocalUseCase.invoke()

  @Bindable
  var adapter = CommentListAdapter()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<CommentListPaginateData>>>(Resource.Default)
  val response = _responseService


  val _responseSend =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)


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
      job = useCase.getComments(exploreId,page)
        .onEach {
          response.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: CommentListPaginateData) {
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

  fun comment(v: View){
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      if(user.name.isEmpty() || user.email.isEmpty() || user.image.isEmpty()){
        showInfo(v.context,v.resources.getString(R.string.please_complete_your_profile))
      }else {
        exploreAction.type = 2
        exploreAction.exploreId = exploreId
        useCase.setExploreAction(exploreAction, true).onEach {
          _responseSend.value = it
        }.launchIn(viewModelScope)
      }
    }
  }
  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

}