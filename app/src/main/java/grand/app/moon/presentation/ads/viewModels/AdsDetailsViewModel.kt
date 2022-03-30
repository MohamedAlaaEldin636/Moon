package grand.app.moon.presentation.ads.viewModels

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import app.grand.tafwak.presentation.reviews.adapters.ReviewsAdapter
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.ads.adapter.PropertyAdapter
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class AdsDetailsViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val useCase: AdsUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  var type: Int = -1
  var id: Int = -1

  @Bindable
  var propertiesAdapter = PropertyAdapter()

  @Bindable
  var reviewsAdapter = ReviewsAdapter()

  @Bindable
  val adsAdapter = AdsAdapter()


  val followStoreRequest = FollowStoreRequest()
  val addFavouriteAdsRequest = AddFavouriteAdsRequest()

  val _adsDetailsResponse =
    MutableStateFlow<Resource<BaseResponse<Advertisement>>>(Resource.Default)
  val adsDetailsResponse = _adsDetailsResponse

  @Bindable
  val advertisement = ObservableField<Advertisement>()
  var isLoggin = userLocalUseCase.isLoggin()


  fun getDetails(id: Int, type: Int) {
    this.id = id
    this.type = type
    useCase.getDetails(id, type)
      .onEach {
        _adsDetailsResponse.value = it
      }.launchIn(viewModelScope)
  }

  fun follow(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      followStoreRequest.storeId = advertisement.get()?.store?.id
      storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
      advertisement.get()?.store?.isFollowing = advertisement.get()?.store?.isFollowing != true
      notifyChange()
    }
  }

  fun back(v: View) {
    v.findNavController().popBackStack()
  }

  fun whatsapp(v: View) {
    var url = "https://api.whatsapp.com/send?phone=${advertisement.get()?.store?.phone}"
    val i = Intent(Intent.ACTION_VIEW)
    url += "&text=" + URLEncoder.encode(
      advertisement.get()?.title + "\n" + advertisement.get()?.description,
      "UTF-8"
    )
    try {
      i.setPackage("com.whatsapp")
      i.data = Uri.parse(url)
      v.context.startActivity(i)
    } catch (e: Exception) {
      try {
        i.setPackage("com.whatsapp.w4b")
        i.data = Uri.parse(url)
        v.context.startActivity(i)
      } catch (exception: Exception) {
        showInfo(v.context, v.context.getString(R.string.please_install_whatsapp_on_your_phone));
      }
    }
  }

  fun phone(v: View) {
    advertisement.get()?.store?.phone?.let { callPhone(v.context, it) }
  }

  fun chat(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      //call follow API
    }
  }

  fun favourite(v: View) {

    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      addFavouriteAdsRequest.advertisementId = advertisement.get()?.id
      useCase.favourite(addFavouriteAdsRequest).launchIn(viewModelScope)
      advertisement.get()?.isFavorite = advertisement.get()?.isFavorite != true
      when (advertisement.get()?.isFavorite) {
        true -> advertisement.get()?.favoriteCount?.plus(1)
        else -> advertisement.get()?.favoriteCount?.minus(1)
      }
      notifyChange()
    }
  }

  fun update(data: Advertisement) {
    advertisement.set(data)
    propertiesAdapter.differ.submitList(advertisement.get()?.properties)
    reviewsAdapter.differ.submitList(advertisement.get()?.reviews)
    show.set(true)
  }

  private  val TAG = "AdsDetailsViewModel"
  fun recallApi(isAuthorize: Boolean) {
    Log.d(TAG, "recallApi: recallApi recallApi")
    if (!isLoggin && isAuthorize){
      Log.d(TAG, "recallApi: DONER")
      isLoggin = isAuthorize
      getDetails(id, type)
    }
  }
}