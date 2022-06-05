package grand.app.moon.presentation.ads.viewModels

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import com.google.android.gms.maps.MapFragment
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.presentation.ads.AdsDetailsFragment
import grand.app.moon.presentation.ads.AdsListFragment
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.category.view.CategoryDetailsFragment
import grand.app.moon.presentation.filter.result.FilterResultsFragment
import grand.app.moon.presentation.home.HomeFragment
import grand.app.moon.presentation.search.views.SearchFragment
import grand.app.moon.presentation.store.views.StoreDetailsFragment
import grand.app.moon.presentation.store.views.StoreFollowedListFragment
import grand.app.moon.presentation.store.views.StoreListFragment
import grand.app.moon.presentation.subCategory.SubCategoryFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
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
  fun details(v: View) {
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
    if(v.context.isLoginWithOpenAuth()) {
      viewModelScope.launch(Dispatchers.IO) {
        adsRepository.setInteraction(InteractionRequest(advertisement.id.toString(), 7))
      }
    }
    shareWhatsapp(
      v,
      advertisement.title,
      advertisement.description,
      advertisement.country.country_code + advertisement.phone
    )
  }

  fun phone(v: View) {
    if(v.context.isLoginWithOpenAuth()) {
      viewModelScope.launch(Dispatchers.IO) {
        adsRepository.setInteraction(InteractionRequest(advertisement.id.toString(), 6))
      }
    }
    callPhone(v.context, advertisement.country.country_code + advertisement.phone)
  }

  //take-care
  fun chat(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      viewModelScope.launch(Dispatchers.IO) {
        adsRepository.setInteraction(InteractionRequest(advertisement.id.toString(), 8))
      }
      advertisement.store?.let {
        v.context.openChatStore(v, it.id, it.name, it.image)
      }
    }
  }

  fun addFavourite(v: View) {
//    advertisement.get()?.isFavorite = advertisement.get()?.isFavorite != true
//    when (advertisement.get()?.isFavorite) {
//      true -> advertisement.get()?.favoriteCount?.plus(1)
//      else -> advertisement.get()?.favoriteCount?.minus(1)
//    }
    if (v.context.isLoginWithOpenAuth()) {
      val fav = !advertisement.isFavorite
      if(fav){
        advertisement.favoriteCount ++
      }else advertisement.favoriteCount--

      advertisement.isFavorite = fav
      ListHelper.addOrUpdate(advertisement)
      viewModelScope.launch { adsRepository.favourite(AddFavouriteAdsRequest(advertisementId = advertisement.id)) }
      if (v.context.isLoginWithOpenAuth()) {
        val destination = v.findNavController().currentDestination
        when (destination?.id) {
          R.id.home_fragment -> {
            val fragment = v.findFragment<HomeFragment>()
            fragment.viewModel.adsHomeAdapter.updateFavourite()
          }
          R.id.categoryDetailsFragment -> {
            val fragment = v.findFragment<CategoryDetailsFragment>()
            fragment.viewModel.adsHomeAdapter.updateFavourite()
          }
          R.id.searchFragment -> {
            val fragment = v.findFragment<SearchFragment>()
            fragment.viewModel.adapter.updateFavourite()
          }
          R.id.adsDetailsFragment -> {
            val fragment = v.findFragment<AdsDetailsFragment>()
            fragment.viewModel.adsAdapter.updateFavourite()
          }
          R.id.navCategoryListAds -> {
            val fragment = v.findFragment<SubCategoryFragment>()
            fragment.viewModel.adapter.updateFavourite()
          }
          R.id.filterResultsFragment -> {
            val fragment = v.findFragment<FilterResultsFragment>()
            fragment.viewModel.adapter.updateFavourite()
          }
          R.id.storeDetailsFragment -> {
            val fragment = v.findFragment<StoreDetailsFragment>()
            fragment.viewModel.adsAdapter.updateFavourite()
          }
          R.id.adsListFragment , R.id.advertisementListFragment -> {
            val fragment = v.findFragment<AdsListFragment>()
            fragment.viewModel.adapter.updateFavourite()
          }
          R.id.mapFragment -> {
            val fragment = v.findFragment<grand.app.moon.presentation.map.MapFragment>()
            fragment.viewModel.updateFavourite()
          }
        }
      }
    }
  }

}