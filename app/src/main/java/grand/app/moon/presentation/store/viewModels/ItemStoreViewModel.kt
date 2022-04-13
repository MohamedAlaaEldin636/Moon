package grand.app.moon.presentation.store.viewModels

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants

class ItemStoreViewModel(val store: Store,var percentage: Int) : BaseViewModel(){

  fun storeDetails(v: View){
    v.findNavController().navigate(
      R.id.nav_store,
      bundleOf(
        "id" to store.id
      ),Constants.NAVIGATION_OPTIONS)
  }
}