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
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.models.User
import grand.app.moon.core.extenstions.convertToString
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.ShareRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.openBrowser
import grand.app.moon.presentation.explore.adapter.ExploreGridEqualAdapter
import grand.app.moon.presentation.store.views.StoreDetailsFragmentDirections
import grand.app.moon.presentation.subCategory.PropertiesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class StoreDetailsViewModel @Inject constructor(
  var adsRepository: AdsRepository,
  val userLocalUseCase: UserLocalUseCase,
  private val useCase: AdsUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  var mapConfig: MapConfig? = null
  val gridOne = ObservableBoolean(true)

  var id: Int = -1
  var type: Int = 3
  var sortBy = 1

  //https://maps.googleapis.com/maps/api/staticmap?center=Berkeley,CA&zoom=14&size=400x400&key=AIzaSyApcEA5RXncL4762cObXGeBaE1x-nEZpOM

  @Bindable
  var adsAdapter: AdsAdapter = AdsAdapter(adsRepository)

  val propertiesAdapter = PropertiesAdapter()

  @Bindable
  var exploreAdapter = ExploreGridEqualAdapter()

  val followStoreRequest = FollowStoreRequest()

  val _storeDetailsResponse =
    MutableStateFlow<Resource<BaseResponse<Store>>>(Resource.Default)
  val storeDetailsResponse = _storeDetailsResponse

  @Bindable
  val store = ObservableField(Store())
  var isLoggin = userLocalUseCase.isLoggin()
  val image = ObservableField<String>("")
  val isOnline = ObservableBoolean(false)

  val showAds = ObservableBoolean(true)
  val showGallery = ObservableBoolean(false)

  val facebook = ObservableBoolean(false)
  val instgram = ObservableBoolean(false)
  val youtube = ObservableBoolean(false)
  val twitter = ObservableBoolean(false)

  var blockStore = false

  init {
    adsAdapter.fromStore = true
    adsAdapter.percentageAds = 100
    adsAdapter.showFavourite = true
  }

  fun getDetails(id: Int, type: Int) {
    this.id = id
    this.type = type
    storeUseCase.storeDetails(id, type)
      .onEach {
        _storeDetailsResponse.value = it
      }.launchIn(viewModelScope)
  }

  val mail = ObservableField(MyApplication.instance.getString(R.string.show_mail))
  fun showMail(v: View) {
    mail.set(store.get()?.email)
  }

  fun follow(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      followStoreRequest.storeId = store.get()?.id
      storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
      Log.d(TAG, "follow: ${store.get()!!.followersCount}")
      store.get()?.isFollowing = store.get()?.isFollowing != true
      var followers: Int = store.get()!!.followersCount.toInt()
      if(store.get()!!.isFollowing) followers++
      else followers--
      store.get()!!.followersCount = followers.toString()
//      store.get()?.followersCount =
//        if (store.get()?.isFollowing == true) followers++.toString() else followers--.toString()
      store.get()?.id?.let {
        store.get()?.isFollowing?.let { it1 ->
          ListHelper.addFollowStore(
            it,
            it1
          )
        }
      }
      Log.d(TAG, "follow:After ${store.get()!!.followersCount}")

      notifyPropertyChanged(BR.store)
//      notifyChange()
    }
  }


  fun back(v: View) {
    v.findNavController().popBackStack()
  }

  fun share(v: AppCompatImageView) {
    storeUseCase.share(ShareRequest(store.get()?.id))
    store.get()?.share?.let { share(v.context, store.get()?.name + store.get()?.description, it) }
  }

  fun story(): String {
    val textStory = if (store.get()!!.stories.size > 0) store.get()!!.stories[0].file else ""
    Log.d(TAG, "story: ${textStory}")
    return textStory
  }

  fun story(v: View) {
    val text: String = v.context.convertToString(store.get()!!)
    Log.d(TAG, "storyHERE: $text")
    val uri = Uri.Builder()
      .scheme("store")
      .authority("grand.app.moon.story.List")
      .appendPath(text)
      .build()
    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
    v.findNavController().navigate(request)


  }

  fun zoomImage(v: View) {
    v.findNavController()
      .navigate(
        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToZoomFragment2(
          store.get()!!.backgroundImage
        )
      )
  }

  fun rates(v: View) {
    v.findNavController()
      .navigate(
        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToReviewsFragment2(
          store.get()!!.id,
          Constants.FOLLOWERS,
          v.resources.getString(R.string.rates)
        )
      )
  }


  fun followers(v: View) {
    v.findNavController().navigate(
      StoreDetailsFragmentDirections.actionStoreDetailsFragmentToStoreUsersFragment(
        store.get()!!.id,
        Constants.FOLLOWERS,
        v.resources.getString(R.string.followers)
      )
    )
  }

  fun seen(v: View) {
    v.findNavController().navigate(
      StoreDetailsFragmentDirections.actionStoreDetailsFragmentToStoreUsersFragment(
        store.get()!!.id,
        Constants.VIEWS,
        v.resources.getString(R.string.views)
      )
    )
  }


  fun whatsapp(v: View) {
    viewModelScope.launch(Dispatchers.IO) {
      adsRepository.setInteraction(InteractionRequest(store_id = store.get()?.id, type = 7))
    }
    var url =
      "https://api.whatsapp.com/send?phone=${store.get()?.country?.country_code + store.get()?.phone}"
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
    if (v.context.isLoginWithOpenAuth()) {
      v.findNavController().navigate(
        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToReportDialog(
          v.resources.getString(R.string.please_choose_report_reason),
          7,
          store.get()!!.id, -1
        )
      )
    }
  }

  fun phone(v: View) {
    viewModelScope.launch(Dispatchers.IO) {
      adsRepository.setInteraction(InteractionRequest(store_id = store.get()?.id, type = 6))
    }
    store.get()?.phone?.let { callPhone(v.context, store.get()?.country?.country_code + it) }
  }

  fun block(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      v.findNavController().navigate(
        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToReportDialog(
          v.resources.getString(R.string.please_choose_block_reason),
          8,
          store.get()!!.id, -1
        )
      )
    }
  }

  fun chat(v: View) {
    viewModelScope.launch(Dispatchers.IO) {
      adsRepository.setInteraction(InteractionRequest(store_id = store.get()?.id, type = 8))
    }
    if (v.context.isLoginWithOpenAuth()) {
      store.get()?.let {
        v.context.openChatStore(v, it.id, it.name, it.image)
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

  fun checkOnline() {
    val listenerID = "store_${store.get()!!.id}";

    CometChat.addUserListener(
      listenerID, object : CometChat.UserListener() {
        override fun onUserOnline(p0: User?) {
          super.onUserOnline(p0)
          isOnline.set(true)
        }

        override fun onUserOffline(p0: User?) {
          super.onUserOffline(p0)
          isOnline.set(false)
        }
      }
    );
  }

  fun update(keyMap: String, data: Store, days: ArrayList<String>) {
    data.workingHours?.forEachIndexed() { index, element ->
      data.workingHours?.get(index)?.day = days[index]
    }
    if (data.latitude != 0.0 && data.longitude != 0.0) {
      image.set(
        "https://maps.googleapis.com/maps/api/staticmap?center=${data.latitude},${data.longitude}" +
          "&markers=icon:${data.image}|${data.latitude},${data.longitude}" +
          "&zoom=14&size=400x400&key=$keyMap"
      )
      Log.d(TAG, "update: ${image.get()}")
    }

//    Log.d(TAG, "update: ${data.advertisements.size}")
    store.set(data)
    checkOnline()
    adsAdapter.differ.submitList(data.advertisements)
    propertiesAdapter.selected = 0
    propertiesAdapter.differ.submitList(data.category)
    val storeItem = Store()
    data.explore.forEach { explore ->
      storeItem.id = data.id
      storeItem.image = data.image
      storeItem.name = data.name
      storeItem.nickname = data.nickname
      explore.store = storeItem

    }
    exploreAdapter.differ.submitList(data.explore)
//    store.get()?.explore?.let { exploreAdapter.differ.currentList.addAll(it) }
    notifyPropertyChanged(BR.exploreAdapter)
//    exploreAdapter.notifyDataSetChanged()

    store.get()?.socialMediaLinks?.forEach { socialLink ->
      if (socialLink.type == "facebook")
        facebook.set(true)
      if (socialLink.type == "twitter")
        twitter.set(true)
      if (socialLink.type == "youtube")
        youtube.set(true)
      if (socialLink.type == "instgram")
        instgram.set(true)
    }

    notifyPropertyChanged(BR.adsAdapter)
    notifyChange()
//    Log.d(TAG, "explore: ${exploreAdapter.list.size}")
    show.set(true)
  }

  private val TAG = "AdsDetailsViewModel"
  fun recallApi(isAuthorize: Boolean) {
    Log.d(TAG, "recallApi: recallApi recallApi")
    if (!isLoggin && isAuthorize) {
      Log.d(TAG, "recallApi: DONER")
      isLoggin = isAuthorize
      getDetails(id, type)
    }
  }

  val listExplores = ArrayList<Explore>()
  fun exploreDetails(position: Int, v: View) {
    val list = ArrayList(exploreAdapter.differ.currentList)
    listExplores.clear()
    listExplores.addAll(list)
    Collections.swap(listExplores, 0, position);

    val data = v.context.convertToString(listExplores)

    Log.d(TAG, "exploreDetails : $position : $data")
    val uri = Uri.Builder()
      .scheme("explore")
      .authority("grand.app.moon.explore.list")
      .appendPath(data)
      .build()
    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
    v.findNavController().navigate(request)


  }

  fun filter(v: View) {
    store.get()?.id?.let { toFilter(v, store_id = it, store = store.get()!!) }
  }


  fun filterSort(v: View) {
    v.findNavController().navigate(
      StoreDetailsFragmentDirections.actionStoreDetailsFragmentToFilterSortDialog2(
        sortBy,
        FilterDialog.ADVERTISEMENT
      )
    )
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

  fun setSortAds(sortBy: Int) {
    this.sortBy = sortBy
    val list = ArrayList(adsAdapter.differ.currentList)
    when (sortBy) {
      1 -> { // الاحدث
        val sortedList = list.sortedWith(compareBy { it.date }).reversed()
        adsAdapter.differ.submitList(null)
        adsAdapter.differ.submitList(sortedList)
      }
      2 -> { // الاقدم
        val sortedList = list.sortedWith(compareBy { it.date })
        adsAdapter.differ.submitList(null)
        adsAdapter.differ.submitList(sortedList)
      }
      3 -> { // الاعلى سعرا
        val sortedList = list.sortedWith(compareBy { it.price }).reversed()
        sortedList.forEach {
          Log.d(TAG, "setSortAdsHigh: ${it.price}")
        }
        adsAdapter.differ.submitList(null)
        adsAdapter.differ.submitList(sortedList)
      }
      4 -> { // الاقل سعراا
        val sortedList = list.sortedWith(compareBy { it.price })
        sortedList.forEach {
          Log.d(TAG, "setSortAdsLow: ${it.price}")
        }
        adsAdapter.differ.submitList(null)
        adsAdapter.differ.submitList(sortedList)
      }
    }
  }

  //sort ads By Category
  fun propertySelect() {
    propertiesAdapter.submitSelect()
    if (propertiesAdapter.position == 0) { // show-all
      adsAdapter.differ.submitList(null)
      adsAdapter.differ.submitList(store.get()?.advertisements)
    } else {
      val id = propertiesAdapter.differ.currentList[propertiesAdapter.position].id
      val list = ArrayList<Advertisement>()
      Log.d(TAG, "propertySelect: ${id}")
      store.get()?.advertisements?.forEach {
        Log.d(TAG, "propertySelect: ${it.id}")
        Log.d(TAG, "propertySelect_category: ${it.categoryId}")
        if (it.categoryId == id)
          list.add(it)
      }
      adsAdapter.differ.submitList(null)
      adsAdapter.differ.submitList(list)
    }
  }

  fun changeGrid() {
    if (adsAdapter.grid == Constants.GRID_2) {
      adsAdapter.grid = Constants.GRID_1
      clickEvent.value = Constants.GRID_1
    } else {
      adsAdapter.grid = Constants.GRID_2
      clickEvent.value = Constants.GRID_2
    }
  }
}