package grand.app.moon.presentation.store.viewModels

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.settings.data_source.remote.SettingsServices
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.openBrowser
import grand.app.moon.presentation.store.views.StoreDetailsFragmentDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception
import java.net.URLEncoder
import javax.inject.Inject


@HiltViewModel
class StoreDetailsViewModel @Inject constructor(
  var settingsServices: SettingsServices,
  val userLocalUseCase: UserLocalUseCase,
  private val useCase: AdsUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  var id: Int = -1


  @Bindable
  val adsAdapter = AdsAdapter()


  val followStoreRequest = FollowStoreRequest()
  val addFavouriteAdsRequest = AddFavouriteAdsRequest()

  val _storeDetailsResponse =
    MutableStateFlow<Resource<BaseResponse<Store>>>(Resource.Default)
  val storeDetailsResponse = _storeDetailsResponse

  @Bindable
  val store = ObservableField<Store>()
  var isLoggin = userLocalUseCase.isLoggin()


  init {
    adsAdapter.percentageAds = 100
  }

  fun getDetails(id: Int) {
    this.id = id
    storeUseCase.storeDetails(id)
      .onEach {
        _storeDetailsResponse.value = it
      }.launchIn(viewModelScope)
  }

  fun follow(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      followStoreRequest.storeId = store.get()?.id
      storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
      store.get()?.isFollowing = store.get()?.isFollowing != true
      notifyChange()
    }
  }

  fun back(v: View) {
    v.findNavController().popBackStack()
  }

  fun whatsapp(v: View) {
    var url = "https://api.whatsapp.com/send?phone=${store.get()?.phone}"
    val i = Intent(Intent.ACTION_VIEW)
    url += "&text=" + URLEncoder.encode(
      store.get()?.name,
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

  fun workingHours(v: View) {
    store.get()?.let {
      v.findNavController().navigate(
        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToWorkingHoursDialog(
          it
        )
      )
    }

  }

  fun report(v: View) {

  }

  fun phone(v: View) {
    store.get()?.phone?.let { callPhone(v.context, it) }
  }

  fun block(v: View) {

  }

  fun chat(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      store.get()?.let {
        startChatConversation(v,it.nickname,it.name,it.image)
      }
    }
  }


  fun submit(v: View, type: String) {
    store.get()?.socialMediaLinks?.forEach {
      if (it.type == type)
        openBrowser(v.context, it.link)
    }
  }

  fun map(v: View) {
    val url =
      "https://www.google.com/maps/dir/?api=1&destination=" + store.get()?.latitude + "," + store.get()?.longitude + "&travelmode=driving"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    v.context.startActivity(intent)
  }

  fun update(data: Store, days: ArrayList<String>) {
    data.workingHours?.forEachIndexed() { index, element ->
      data.workingHours?.get(index)?.day = days[index]
    }
    store.set(data)
    adsAdapter.differ.submitList(store.get()?.advertisements)
    show.set(true)
  }

  private val TAG = "AdsDetailsViewModel"
  fun recallApi(isAuthorize: Boolean) {
    Log.d(TAG, "recallApi: recallApi recallApi")
    if (!isLoggin && isAuthorize) {
      Log.d(TAG, "recallApi: DONER")
      isLoggin = isAuthorize
      getDetails(id)
    }
  }
}