package grand.app.moon.presentation.ads.viewModels

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import grand.app.moon.NavAdListArgs
import grand.app.moon.NavCategoryListAdsArgs
import grand.app.moon.R
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants

class ItemAdsHomeViewModel constructor(val category: CategoryAdvertisement) : BaseViewModel() {
  val adapter = AdsAdapter()
  private val TAG = "ItemAdsHomeViewModel"

  init {
    Log.d(TAG, ": ADAPTER " + category.advertisements.size)
    adapter.differ.submitList(category.advertisements)
  }

  fun showAll(v: View) {
    val builder = NavCategoryListAdsArgs.Builder()
    builder.categoryId = category.id
    builder.tabBarText = category.name
    if(category.type != -1) {
      builder.isSub = false
      builder.type = category.type
    }
    v.findNavController()
      .navigate(
        R.id.nav_category_list_ads, builder.build().toBundle(),
        Constants.NAVIGATION_OPTIONS
      )

  }
}