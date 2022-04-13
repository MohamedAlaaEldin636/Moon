package grand.app.moon.presentation.map.viewModel

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants

class ItemCategoryTextViewModel  constructor(val category: CategoryItem, var selected :Boolean) : BaseViewModel(){
}