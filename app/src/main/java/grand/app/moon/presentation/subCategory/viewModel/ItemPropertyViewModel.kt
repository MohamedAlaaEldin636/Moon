package grand.app.moon.presentation.subCategory.viewModel

import android.util.Log
import android.view.View
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.home.models.Property
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.store.views.StoreDetailsFragment
import grand.app.moon.presentation.subCategory.SubCategoryFragment

class ItemPropertyViewModel  constructor(val category: Property, var position: Int, var selected :Boolean) : BaseViewModel(){

  private  val TAG = "ItemPropertyViewModel"
  fun submit(v: View){
    val id = v.findNavController().currentDestination?.id
    when(id){
      R.id.navCategoryListAds -> {
        val fragment = v.findFragment<SubCategoryFragment>()
        fragment.viewModel.propertiesAdapter.position = position
        Log.d(TAG, "submit: FOIND")
        fragment.viewModel.propertySelect()
      }
      R.id.storeDetailsFragment -> {
        val fragment = v.findFragment<StoreDetailsFragment>()
        fragment.viewModel.propertiesAdapter.position = position
        Log.d(TAG, "submit: FOIND")
        fragment.viewModel.propertySelect()
      }
    }
  }
}