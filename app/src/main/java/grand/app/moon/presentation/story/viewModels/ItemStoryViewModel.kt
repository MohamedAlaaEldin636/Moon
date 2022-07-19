package grand.app.moon.presentation.story.viewModels

import android.util.Log
import android.view.View
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.BaseViewModel

class ItemStoryViewModel constructor(val store: Store) : BaseViewModel() {

  private val TAG = "ItemStoryViewModel"

  init {
    Log.d(TAG, ": ${store.premium}")
  }

  fun getStory(): StoryItem {
    return if (store.stories.size > 0) store.stories[0] else StoryItem()
  }
}