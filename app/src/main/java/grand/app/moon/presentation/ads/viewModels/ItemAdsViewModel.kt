package grand.app.moon.presentation.ads.viewModels

import android.content.Intent
import android.view.View
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.presentation.ads.AdsDetailsFragment
import grand.app.moon.presentation.ads.AdsDetailsFragmentDirections
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseViewModel

class ItemAdsViewModel(
  var advertisement: Advertisement,
  var percentageAds: Int
) : BaseViewModel(){
  val adapter = AdsAdapter()
  init {
  }



  fun whatsapp(v: View){
    advertisement.store?.phone?.let {
      shareWhatsapp(v,advertisement.title,advertisement.description,
        it
      )
    }
  }


  fun phone(v: View) {
    advertisement.store?.phone?.let { callPhone(v.context, it) }
  }

  //take-care
  fun chat(v: View) {
    if(v.context.isLoginWithOpenAuth()) {
      advertisement.store?.let {
        v.context.openChatStore(v, it.id, it.name, it.image)
      }
    }
  }

}