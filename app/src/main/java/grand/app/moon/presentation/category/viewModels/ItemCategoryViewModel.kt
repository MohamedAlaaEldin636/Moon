package grand.app.moon.presentation.category.viewModels

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.view.CategoryDetailsFragmentDirections

class ItemCategoryViewModel constructor(val category: CategoryItem, var percentage: Int) :
  BaseViewModel() {


  fun submit(v: View) {
    if (category.total == null) {
      if (category.subCategories != null) {
        v.findNavController().navigate(
          R.id.categoryDetailsFragment,
          bundleOf(
            "category_id" to category.id,
            "tabBarText" to category.name
          ), Constants.NAVIGATION_OPTIONS
        )
      } else {
        v.findNavController().navigate(
          R.id.subCategoryFragment,
          bundleOf(
            "sub_category_id" to category.id,
            "tabBarText" to category.name
          ), Constants.NAVIGATION_OPTIONS
        )
      }
    } else {
      v.findNavController().navigate(CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToAdsListFragment2(category.name))
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