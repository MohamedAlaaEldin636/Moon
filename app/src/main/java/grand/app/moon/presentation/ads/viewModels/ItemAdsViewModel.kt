package grand.app.moon.presentation.ads.viewModels

import android.util.Log
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
  val type: Int,
  val fromStore: Boolean = false
) : BaseViewModel() {
//  val adapter = AdsAdapter(adsRepository)

  private val TAG = "ItemAdsViewModel"
  fun details(v: View){
    Log.d(TAG, "details: $type")
    v.findNavController().navigate(
      R.id.nav_ads, bundleOf(
        "id" to advertisement.id,
        "type" to type,
        "from_store" to fromStore
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