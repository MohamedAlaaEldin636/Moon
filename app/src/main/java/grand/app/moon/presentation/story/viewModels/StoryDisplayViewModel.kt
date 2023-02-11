package grand.app.moon.presentation.story.viewModels

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.core.extenstions.startChatPage
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.domain.store.entity.StoryRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.storyView.data.Story
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StoryDisplayViewModel @Inject constructor(
  val storeUseCase: StoreUseCase,
  application: Application,
) : BaseViewModel() {

	val storyLink = MutableLiveData<StoryLink?>()
	val phone = MutableLiveData("")
	val drawableOfStoryLink = storyLink.map {
		val drawableRes = when (it) {
			StoryLink.WHATSAPP -> R.drawable.story_whatsapp
			StoryLink.CALL -> R.drawable.story_call
			StoryLink.CHAT -> R.drawable.story_chat
			null -> return@map null
		}

		ContextCompat.getDrawable(application, drawableRes)
	}
	val textOfStoryLink = storyLink.map {
		val textRes = when (it) {
			StoryLink.WHATSAPP -> R.string.whatsapp_3
			StoryLink.CALL -> R.string.chat_3829
			StoryLink.CHAT -> R.string.call
			null -> return@map null
		}

		application.getString(textRes)
	}

  var fromStore: Boolean = false
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
        v.context.openChatStore(v, it.id.orZero(), it.name.orEmpty(), it.image)
      }
    }
  }

  fun like(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      isLike.set(!isLike.get())
      store.get()?.stories?.getOrNull(pos)?.is_liked = isLike.get()
      ListHelper.addLike(store.get()?.stories?.getOrNull(pos)?.id.orZero(), isLike.get())
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
    return pos < store.get()?.stories?.size.orZero() - 1
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
      ListHelper.addViewStory(stories?.getOrNull(positionStoryAdapter)?.id.orZero())
      positionStoryAdapter++
      store.set(stories[positionStoryAdapter])
      return true
    }
    return false
  }

  fun share(v: View) {
    share(v.findFragment<Fragment>().requireActivity(), store.get()?.name.orEmpty(), store.get()?.stories?.getOrNull(pos)?.shareLink.orEmpty())
  }



  fun checkBlockStore(): Boolean {
    stories.forEach {
      if(ListHelper.checkBlockStore(it.id.orZero()))
        return true
    }
    return false
  }
}