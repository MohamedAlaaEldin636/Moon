package grand.app.moon.presentation.ads.viewModels

import android.util.Log
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel

class ItemAdsHomeViewModel  constructor(val category: CategoryAdvertisement) : BaseViewModel(){
  val adapter = AdsAdapter()
  private  val TAG = "ItemAdsHomeViewModel"
  init {
    Log.d(TAG, ": ADAPTER "+category.advertisements.size)
    adapter.differ.submitList(category.advertisements)
  }
}