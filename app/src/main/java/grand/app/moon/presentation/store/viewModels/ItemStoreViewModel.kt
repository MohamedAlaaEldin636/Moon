package grand.app.moon.presentation.store.viewModels

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.views.StoreBlockListFragment
import grand.app.moon.presentation.store.views.StoreFollowedListFragment
import grand.app.moon.presentation.store.views.StoreListFragment
import kotlinx.coroutines.flow.launchIn

//store/unblock
class ItemStoreViewModel(val store: Store,val type: Int,var percentage: Int,var useCase: StoreUseCase?,var position : Int) : BaseViewModel(){

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
      }
    }
  }

  fun cancelBlock(v: View){
    val fragment = v.findFragment<StoreBlockListFragment>()
    fragment.viewModel.adapter.position = position
    fragment.viewModel.unBlock()
  }
}