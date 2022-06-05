package grand.app.moon.presentation.story.viewModels

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.core.extenstions.startChatPage
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.StoryRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.story.entity.StoryItem
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
  var positionStoryAdapter: Int = 0
  var pos: Int = -1
  var stories: ArrayList<Store> = arrayListOf()
  var progress = ObservableField(true)
  var image = ObservableField<String>("")
  var isLoaded = false
  val store = ObservableField<Store>()
  val isLike = ObservableBoolean(false)
  var isFinish = false
//  val listStories = arrayListOf()

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

  fun chat(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      store.get()?.let {
        v.context.openChatStore(v, it.id, it.name, it.image)
      }
    }
  }

  fun like(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      isLike.set(!isLike.get())
      store.get()!!.stories[pos].is_liked = isLike.get()
      ListHelper.addLike(store.get()!!.stories[pos].id, isLike.get())
      storyRequest.type = 2
      callService()
    }
  }

  fun callService() {
    storeUseCase.storyAction(storyRequest)
      .onEach {

      }.launchIn(viewModelScope)
  }

  private val TAG = "StoryDisplayViewModel"
  fun havePrevStory(): Boolean {
    Log.d(TAG, "havePrevStory: $pos")
    if (pos > 0) {
      pos--
      store.set(stories[pos])
      return true
    }
    return false
  }

  fun allowNext(): Boolean {
    return pos < store.get()!!.stories.size - 1
  }

  fun allowPrev(): Boolean {
    return pos > 0
  }

  fun prevStory(): Boolean {
    if (positionStoryAdapter > 0) {
      positionStoryAdapter--
      store.set(stories[positionStoryAdapter])
      return true
    }
    return false
  }

  fun nextStory(): Boolean {
    if (positionStoryAdapter < stories.size - 1) {
      ListHelper.addViewStory(stories[positionStoryAdapter].id)
      positionStoryAdapter++
      store.set(stories[positionStoryAdapter])
      return true
    }
    return false
  }

  fun share(v: View) {
    shareTitleMessageImage(v.findFragment<Fragment>().requireActivity(), store.get()!!.name, store.get()!!.description, store.get()!!.stories[pos].file)
  }
}