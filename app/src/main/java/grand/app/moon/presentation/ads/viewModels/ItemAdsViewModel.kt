package grand.app.moon.presentation.ads.viewModels

import android.view.View
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import javax.inject.Inject

class ItemAdsViewModel (
  var advertisement: Advertisement) : BaseViewModel(){
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