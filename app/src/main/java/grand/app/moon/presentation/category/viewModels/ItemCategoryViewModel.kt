package grand.app.moon.presentation.category.viewModels

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import grand.app.moon.NavAdListArgs
import grand.app.moon.NavCategoryListAdsArgs
import grand.app.moon.R
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.view.CategoryDetailsFragmentDirections

class ItemCategoryViewModel constructor(val category: CategoryItem, var percentage: Int) :
  BaseViewModel() {

  private  val TAG = "ItemCategoryViewModel"
  init {
    Log.d(TAG, ": ${category.name}")
  }

  fun submit(v: View) {
	  MyLogger.e("abcde 1")
    if (category.total == null) {
	    MyLogger.e("abcde 2")
      if (category.subCategories != null) {
	      MyLogger.e("abcde 3")
        Log.d(TAG, "submit: category id")
        v.findNavController().navigate(
          R.id.categoryDetailsFragment,
          bundleOf(
            "category_id" to category.id,
            "tabBarText" to category.name
          ), Constants.NAVIGATION_OPTIONS
        )
      } else {
	      MyLogger.e("abcde 4")
        Log.d(TAG, "submit: sub_category id")
        v.findNavController().navigate(
          R.id.nav_category_list_ads,
          bundleOf(
            "category_id" to category.categoryId,
            "sub_category" to category.id,
            "tabBarText" to category.name
          ), Constants.NAVIGATION_OPTIONS
        )
      }
    } else {
	    MyLogger.e("abcde 5")


//      CategoryDetailsFragmentDirections
//        .actionCategoryDetailsFragmentToNavAdList(category.name,category.id!!)
//
      val builder = NavCategoryListAdsArgs.Builder()
      builder.categoryId = category.id!!
      builder.tabBarText = category.name

      v.findNavController()
        .navigate(
          R.id.nav_category_list_ads, builder.build().toBundle(),
          Constants.NAVIGATION_OPTIONS
        )
//      v.findNavController().navigate(
//        R.id.adsListFragment2,
//        bundleOf(
//          "category_id" to category.id,
//          "tabBarText" to category.name
//        ), Constants.NAVIGATION_OPTIONS
//      )
    }
  }
}