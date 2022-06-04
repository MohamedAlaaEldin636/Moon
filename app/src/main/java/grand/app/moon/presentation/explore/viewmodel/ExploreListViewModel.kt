package grand.app.moon.presentation.explore.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.presentation.notification.adapter.ExploreListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.use_case.LogInUseCase
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
class ExploreListViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val useCase: ExploreUseCase,
  private val storeUseCase: StoreUseCase,
  private val exploreUseCase: ExploreUseCase,

) : BaseViewModel() {
  @Bindable
  var page: Int = 0
  @Bindable
  var callingService = false

  var type:Int = -1

  var isLast = false
  var isLoggin = userLocalUseCase.isLoggin()

  val exploreAction = ExploreAction()

  @Bindable
  var adapter = ExploreListAdapter()

  val followStoreRequest = FollowStoreRequest()

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<ExploreListPaginateData>>>(Resource.Default)

  val response = _responseService

  val user = userLocalUseCase.invoke()
  init {
    adapter.user = user
    adapter.exploreUseCase = exploreUseCase
  }


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

  private val TAG = "PackagesViewModel"

  fun setData(data: ExploreListPaginateData) {
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
  fun follow() {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      followStoreRequest.storeId = adapter.differ.currentList[adapter.position].store.id
      storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
      adapter.differ.currentList[adapter.position].store.isFollowing = !adapter.differ.currentList[adapter.position].store.isFollowing
      adapter.changeFollowing(adapter.differ.currentList[adapter.position].store.id,adapter.differ.currentList[adapter.position].store.isFollowing)
      ListHelper.addFollowStore(adapter.differ.currentList[adapter.position].store.id,adapter.differ.currentList[adapter.position].store.isFollowing)
      adapter.notifyItemChanged(adapter.position)
    }
  }

  fun fav() {
    Log.d(TAG, "fav: FAVOURUTE")
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      Log.d(TAG, "fav: HERE")
      val explore = adapter.differ.currentList[adapter.position]
      exploreAction.exploreId = explore.id
      exploreAction.type = 1
      Log.d(TAG, "fav: ${exploreAction.exploreId}")
      Log.d(TAG, "fav: ${exploreAction.type}")
      useCase.setExploreAction(exploreAction).launchIn(viewModelScope)
      explore.isLike = !explore.isLike
      if(explore.isLike){
        explore.likes ++
      }else explore.likes--

      adapter.notifyItemChanged(adapter.position)
    }
  }

  fun share(v: View){
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      val explore = adapter.differ.currentList[adapter.position]
      exploreAction.exploreId = explore.id
      exploreAction.type = 3
      explore.shares++
      adapter.notifyItemChanged(adapter.position)
      shareTitleMessageImage(v.findFragment<Fragment>().requireActivity(),explore.store.name,explore.store.description,explore.file)
    }

  }


}