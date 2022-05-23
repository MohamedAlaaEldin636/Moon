package grand.app.moon.presentation.ads.viewModels

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ItemAdsViewModel(
  var advertisement: Advertisement,
  var percentageAds: Int,
  val adsRepository: AdsRepository,
  val showFavourite: Boolean = false,
  val type: Int
) : BaseViewModel() {
  val adapter = AdsAdapter(adsRepository)

  fun details(v: View){
    v.findNavController().navigate(
      R.id.nav_ads, bundleOf(
        "id" to advertisement.id,
        "type" to type
      )
    )
  }

  fun whatsapp(v: View) {
    viewModelScope.launch(Dispatchers.IO) {
      adsRepository.setInteraction(InteractionRequest(advertisement.id.toString(),7))
    }
    shareWhatsapp(v, advertisement.title, advertisement.description, advertisement.country.country_code+advertisement.phone)
  }

  fun phone(v: View) {
    viewModelScope.launch(Dispatchers.IO) {
      adsRepository.setInteraction(InteractionRequest(advertisement.id.toString(),6))
    }
    callPhone(v.context, advertisement.country.country_code+advertisement.phone)
  }

  //take-care
  fun chat(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      viewModelScope.launch(Dispatchers.IO) {
        adsRepository.setInteraction(InteractionRequest(advertisement.id.toString(),8))
      }
      advertisement.store?.let {
        v.context.openChatStore(v, it.id, it.name, it.image)
      }
    }
  }

}