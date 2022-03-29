package grand.app.moon.presentation.ads.viewModels

import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel

class ItemAdsViewModel  constructor(var advertisement: Advertisement) : BaseViewModel(){
  val adapter = AdsAdapter()
  init {
  }
}