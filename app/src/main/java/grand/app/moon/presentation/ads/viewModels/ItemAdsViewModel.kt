package grand.app.moon.presentation.ads.viewModels

import android.view.View
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel

class ItemAdsViewModel(
  var advertisement: Advertisement,
  var percentageAds: Int,
  adsRepository: AdsRepository,
  val showFavourite: Boolean = false
) : BaseViewModel() {
  val adapter = AdsAdapter(adsRepository)

  fun whatsapp(v: View) {
    shareWhatsapp(v, advertisement.title, advertisement.description, advertisement.phone)
  }

  fun phone(v: View) {
    callPhone(v.context, advertisement.phone)
  }

  //take-care
  fun chat(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      advertisement.store?.let {
        v.context.openChatStore(v, it.id, it.name, it.image)
      }
    }
  }

}