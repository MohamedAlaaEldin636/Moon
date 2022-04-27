package grand.app.moon.presentation.subCategory.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import grand.app.moon.domain.subCategory.entity.Property
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.subCategory.SubCategoryFragment

class ItemPropertyViewModel  constructor(val category: Property,var position: Int, var selected :Boolean) : BaseViewModel(){

  fun submit(v: View){
    val fragment = v.findFragment<SubCategoryFragment>()
    fragment.viewModel.propertiesAdapter.position = position
    fragment.viewModel.propertySelect()
  }
}