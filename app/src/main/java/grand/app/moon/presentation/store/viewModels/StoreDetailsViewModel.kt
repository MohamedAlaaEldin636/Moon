package grand.app.moon.presentation.store.viewModels

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.data.settings.data_source.remote.SettingsServices
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.openBrowser
import grand.app.moon.presentation.explore.adapter.ExploreAdapter
import grand.app.moon.presentation.explore.adapter.ExploreWithoutDifferAdapter
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
  lateinit var mapConfig: MapConfig

  var id: Int = -1

  //https://maps.googleapis.com/maps/api/staticmap?center=Berkeley,CA&zoom=14&size=400x400&key=AIzaSyApcEA5RXncL4762cObXGeBaE1x-nEZpOM

  @Bindable
  val adsAdapter = AdsAdapter()

  var exploreAdapter = ExploreWithoutDifferAdapter()

  val followStoreRequest = FollowStoreRequest()

  val _storeDetailsResponse =
    MutableStateFlow<Resource<BaseResponse<Store>>>(Resource.Default)
  val storeDetailsResponse = _storeDetailsResponse

  @Bindable
  val store = ObservableField<Store>()
  var isLoggin = userLocalUseCase.isLoggin()
  val image = ObservableField<String>("")



  val showAds = ObservableBoolean(false)
  val showGallery = ObservableBoolean(true)

  val facebook = ObservableBoolean(true)
  val instgram = ObservableBoolean(true)
  val youtube = ObservableBoolean(true)
  val twitter = ObservableBoolean(true)

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

  fun share(v: AppCompatImageView){
//    val name  = (store.get()?.name != null? "" : "")

    var name = ""
    store.get()?.name?.let {
      name = it
    }
    var description = ""
    store.get()?.description?.let {
      description = it
    }
//    val name = if(store.get()?.name == null) "" else store.get()?.name
    share(v.context, name,description,v)
  }

  fun rates(v: View){

  }


  fun followers(v: View){

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
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      v.findNavController().navigate(
        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToReportDialog(
          v.resources.getString(R.string.please_choose_report_reason),
          7,
          store.get()!!.id
        )
      )
    }
  }

  fun phone(v: View) {
    store.get()?.phone?.let { callPhone(v.context, it) }
  }

  fun block(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      v.findNavController().navigate(
        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToReportDialog(
          v.resources.getString(R.string.please_choose_block_reason),
          8,
          store.get()!!.id
        )
      )
    }
  }

  fun chat(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      store.get()?.let {
        startChatConversation(v, it.nickname, it.name, it.image)
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

  fun update(keyMap: String, data: Store, days: ArrayList<String>) {
    data.workingHours?.forEachIndexed() { index, element ->
      data.workingHours?.get(index)?.day = days[index]
    }
    if(data.latitude != 0.0 && data.longitude != 0.0) {
      image.set(
        "https://maps.googleapis.com/maps/api/staticmap?center=${data.latitude},${data.longitude}" +
          "&markers=icon:${data.image}|${data.latitude},${data.longitude}" +
          "&maptype=satellite&zoom=14&size=400x400&key=$keyMap"
      )
      Log.d(TAG, "update: ${image.get()}")
    }

    store.set(data)
    adsAdapter.differ.submitList(store.get()?.advertisements)

    store.get()?.explore?.let { exploreAdapter.list.addAll(it) }
//    notifyPropertyChanged(BR.exploreAdapter)
    exploreAdapter.notifyDataSetChanged()

    store.get()?.socialMediaLinks?.forEach { socialLink ->
      if(socialLink.link.isEmpty()) {
        if (socialLink.type == "facebook")
          facebook.set(false)
        if (socialLink.type == "twitter")
          twitter.set(false)
        if (socialLink.type == "youtube")
          youtube.set(false)
        if (socialLink.type == "instgram")
          instgram.set(false)
      }
    }

    Log.d(TAG, "explore: ${exploreAdapter.list.size}")
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
  fun showAds() {
    showAds.set(true)
    showGallery.set(false)
    clickEvent.value = Constants.SCROLL_DOWN
  }

  fun showGallery() {
    showGallery.set(true)
    showAds.set(false)
    clickEvent.value = Constants.SCROLL_DOWN
  }
}