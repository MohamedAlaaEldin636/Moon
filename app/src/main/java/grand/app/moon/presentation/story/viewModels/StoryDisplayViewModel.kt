package grand.app.moon.presentation.story.viewModels

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.startChatPage
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.StoryRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.storyView.data.Story
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StoryDisplayViewModel @Inject constructor(
  val storeUseCase: StoreUseCase
) : BaseViewModel() {
  var progress = ObservableField(true)
  var image = ObservableField<String>("")
  val store = ObservableField<Store> ()
  val storyRequest = StoryRequest()
  //1 View, 2 Like, 3 Share
  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun startLoading() {
    progress.set(true)
  }
  fun stopLoading() {
    progress.set(false)
  }

  fun chat(v: View){
    if(v.context.isLoginWithOpenAuth()){
//      v.context.startChatPage(v)
    }
  }

  fun like(v: View){
    if(v.context.isLoginWithOpenAuth()){
      storyRequest.type = 2
    }
  }

  fun callService(){
    storeUseCase.storyAction(storyRequest)
      .onEach {

      }.launchIn(viewModelScope)
  }
}