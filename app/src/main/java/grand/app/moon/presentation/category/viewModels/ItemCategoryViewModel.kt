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

class ItemCategoryViewModel  constructor(val category: CategoryItem,var percentage :Int) : BaseViewModel(){


  fun submit(v: View){
    if(category.subCategories != null) {
      v.findNavController().navigate(
        R.id.categoryDetailsFragment,
        bundleOf(
          "category_id" to category.id,
          "tabBarText" to category.name
        ), Constants.NAVIGATION_OPTIONS
      )
    }else{
      Toast.makeText(v.context, "HERE SUB CATEGORY", Toast.LENGTH_SHORT).show()
    }
  }
}