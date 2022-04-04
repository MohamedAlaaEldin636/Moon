package grand.app.moon.presentation.ads.viewModels

import android.view.View
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.presentation.ads.adapter.AdsAdapter
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

  }

}