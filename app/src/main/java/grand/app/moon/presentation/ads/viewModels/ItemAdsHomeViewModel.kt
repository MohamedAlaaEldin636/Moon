package grand.app.moon.presentation.ads.viewModels

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
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
    val bundle = Bundle()
    if (category.showMore.categoryId != -1) {
      category.showMore.categoryId?.let { bundle.putInt(Constants.CATEGORY_ID, it) }
    }
    if (category.showMore.subCategoryId != -1) {
      category.showMore.subCategoryId?.let { bundle.putInt(Constants.SUB_CATEGORY_ID, it) }
    }
    bundle.putString(Constants.TabBarText, v.resources.getString(R.string.likes))

    v.findNavController().navigate(
      R.id.adsListFragment2,
      bundle, Constants.NAVIGATION_OPTIONS
    )
  }
}