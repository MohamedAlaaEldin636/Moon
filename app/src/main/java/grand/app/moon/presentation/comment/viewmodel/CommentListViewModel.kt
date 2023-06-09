package grand.app.moon.presentation.comment.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.comment.adapter.CommentListAdapter
import grand.app.moon.presentation.comment.adapter.CommentListPaginateAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
  public val userLocalUseCase: UserLocalUseCase,
  private val useCase: ExploreUseCase
) : BaseViewModel() {
  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  @Bindable
  val exploreAction = ObservableField(ExploreAction())
  var exploreId = -1
  var type: Int = -1

  val total = ObservableField<String>("")

  var isLast = false
  var isLoggin = userLocalUseCase.isLoggin()
  var user = userLocalUseCase.invoke()

  @Bindable
  var adapter: CommentListAdapter = CommentListAdapter(userLocalUseCase)

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<CommentListPaginateData>>>(Resource.Default)
//  var response: Flow<PagingData<Comment>>? = null
  val _responseSend =
    MutableStateFlow<Resource<BaseResponse<Comment>>>(Resource.Default)

  val _responseDelete =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)


  init {

  }

  fun callService() {
    Log.d(TAG, "callService: $exploreId")
//    response = useCase.getComments(exploreId)

    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if(page > 1){
        notifyPropertyChanged(BR.page)
      }
      job = useCase.commentListPaginateData(exploreId,page)
        .onEach {
          _responseService.value = it
        }
        .launchIn(viewModelScope)
    }
  }

  private val TAG = "PackagesViewModel"

  suspend fun setData(data: CommentListPaginateData) {
//    data.let {
//      println("size:" + data.list.size)
//      isLast = data.links.next == null
//      adapter.submitData(it.list)
//      callingService = false
//      notifyPropertyChanged(BR.callingService)
//      show.set(true)
//    }
  }

  fun comment(v: View) {
    if(v.context.isLoginWithOpenAuth()){
      exploreAction.get()!!.type = null
      exploreAction.get()!!.exploreId = exploreId
      useCase.setComment(exploreAction.get()!!, true).onEach {
        _responseSend.value = it
      }.launchIn(viewModelScope)
    }
  }

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun deleteComment(position:Int , id: Int) {
    adapter.position = position
    useCase.deleteComment(id).onEach {
      _responseDelete.value = it
    }.launchIn(viewModelScope)
  }

  fun clearModel() {
    exploreAction.get()!!.comment = ""
    notifyPropertyChanged(BR.exploreAction)
  }

}