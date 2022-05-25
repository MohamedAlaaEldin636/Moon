package grand.app.moon.presentation.ads.viewModels

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.Bindable
import androidx.navigation.findNavController
import grand.app.moon.NavCategoryListAdsArgs
import grand.app.moon.R
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import javax.inject.Inject

class ItemAdsHomeViewModel(val category: CategoryAdvertisement, adsRepository: AdsRepository) : BaseViewModel() {

  @Bindable
  var  adapter : AdsAdapter = AdsAdapter(adsRepository)

  private val TAG = "ItemAdsHomeViewModel"

  init {
    adapter.showFavourite = true
    Log.d(TAG, ": ADAPTER " + category.advertisements.size)
    adapter.differ.submitList(category.advertisements)
  }

  fun showAll(v: View) {
//    val builder = NavCategoryListAdsArgs.Builder()
//    builder.tabBarText = category.name
    if(category.type != -1) {
//      builder.isSub = false
//      builder.type = category.type
//      v.findNavController()
//        .navigate(
//          R.id.nav_ad_list, builder.build().toBundle(),
//          Constants.NAVIGATION_OPTIONS
//        )
      v.findNavController().navigate(
        R.id.nav_category_list_ads,
        bundleOf(
          "category_id" to -1,
          "tabBarText" to category.name,
          "type" to category.type
        ), Constants.NAVIGATION_OPTIONS
      )
    }else{
//      builder.categoryId = category.id
      v.findNavController().navigate(
        R.id.nav_category_list_ads,
        bundleOf(
          "category_id" to category.id,
          "tabBarText" to category.name
        ), Constants.NAVIGATION_OPTIONS
      )
    }


  }

  fun title() : String{
    var text= category.name
    if(category.adsCount != -1)
      text += " (${category.adsCount})"
    else
      text += " (${category.advertisements.size})"
    return text
  }
}