package grand.app.moon.presentation.store.viewModels

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.launchIn

class ItemStoreViewModel(val store: Store,var percentage: Int,var useCase: StoreUseCase?,var isLoggin : Boolean) : BaseViewModel(){

  fun storeDetails(v: View){
    v.findNavController().navigate(
      R.id.nav_store,
      bundleOf(
        "id" to store.id
      ),Constants.NAVIGATION_OPTIONS)
  }

  fun follow(v: View){
    if(isLoggin) {
      submitEvent.value = Constants.FOLLOW
      useCase?.follow(FollowStoreRequest(store.id))?.launchIn(viewModelScope)
    }else{
      v.context.startActivity(
        Intent(
          v.context,
          AuthActivity::class.java
        )
      )
    }
  }
}