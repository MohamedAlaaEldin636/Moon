package grand.app.moon.presentation.map.viewModel

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.subCategory.SubCategoryFragment

class ItemCategoryTextViewModel  constructor(val category: CategoryItem, var selected :Boolean) : BaseViewModel(){

  fun submit(v: View){
    val destination = v.findNavController().currentDestination?.id
    if(destination == R.id.navCategoryListAds){

    }
  }
}