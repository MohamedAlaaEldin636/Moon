package grand.app.moon.presentation.story.viewModels

import android.view.View
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.BaseViewModel

class ItemStoryViewModel  constructor(val store: Store) : BaseViewModel(){

  fun getStory(): StoryItem{
    return store.stories[0]
  }
}