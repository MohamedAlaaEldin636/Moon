package grand.app.moon.presentation.store.viewModels

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.view.CategoryDetailsFragment
import grand.app.moon.presentation.home.HomeFragment
import grand.app.moon.presentation.store.views.StoreBlockListFragment
import grand.app.moon.presentation.store.views.StoreFollowedListFragment
import grand.app.moon.presentation.store.views.StoreListFragment

//store/unblock
class ItemStoreViewModel(val store: Store,val type: Int,var percentage: Int,var useCase: StoreUseCase?,var position : Int,var adapterType : Int = -1) : BaseViewModel(){

  fun storeDetails(v: View){
    v.findNavController().navigate(
      R.id.nav_store,
      bundleOf(
        "id" to store.id,
        "type" to type
      ),Constants.NAVIGATION_OPTIONS)
  }

  fun follow(v: View){
    if(v.context.isLoginWithOpenAuth()){
      val destination = v.findNavController().currentDestination
      when(destination?.id){
        R.id.storeFollowedListFragment -> {
          val fragment = v.findFragment<StoreFollowedListFragment>()
          fragment.viewModel.adapter.position = position
          fragment.viewModel.follow()
        }
        R.id.storeListFragment -> {
          val fragment = v.findFragment<StoreListFragment>()
          fragment.viewModel.adapter.position = position
          fragment.viewModel.follow()
        }
        R.id.home_fragment -> {
          val fragment = v.findFragment<HomeFragment>()
          if(adapterType == -1) {//top rated stores
            fragment.viewModel.storeAdapter.position = position
            fragment.viewModel.follow()
          }else { // following Adapter
            fragment.viewModel.followingsStoresAdapter.position = position
            fragment.viewModel.followingsStores()
          }
        }
        R.id.categoryDetailsFragment -> {
          val fragment = v.findFragment<CategoryDetailsFragment>()
          fragment.viewModel.storeAdapter.position = position
          fragment.viewModel.follow()
        }
      }
    }
  }

  fun cancelBlock(v: View){
    val fragment = v.findFragment<StoreBlockListFragment>()
    fragment.viewModel.adapter.position = position
    fragment.viewModel.unBlock()
  }

  fun callPhone(v: View){
    callPhone(v.context,store.country.country_code+store.phone)
  }
}