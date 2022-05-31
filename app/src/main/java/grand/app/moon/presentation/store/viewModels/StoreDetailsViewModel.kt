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
import grand.app.moon.R
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
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
import grand.app.moon.presentation.subCategory.SubCategoryFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URLEncoder
import javax.inject.Inject


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

  var exploreAdapter = ExploreGridEqualAdapter()

  val followStoreRequest = FollowStoreRequest()

  val _storeDetailsResponse =
    MutableStateFlow<Resource<BaseResponse<Store>>>(Resource.Default)
  val storeDetailsResponse = _storeDetailsResponse

  @Bindable
  val store = ObservableField(Store())
  var isLoggin = userLocalUseCase.isLoggin()
  val image = ObservableField<String>("")


  val showAds = ObservableBoolean(true)
  val showGallery = ObservableBoolean(true)

  val facebook = ObservableBoolean(true)
  val instgram = ObservableBoolean(true)
  val youtube = ObservableBoolean(true)
  val twitter = ObservableBoolean(true)

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

  fun follow(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      followStoreRequest.storeId = store.get()?.id
      storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
      store.get()?.isFollowing = store.get()?.isFollowing != true
      store.get()?.id?.let {
        store.get()?.isFollowing?.let { it1 ->
          ListHelper.addFollowStore(
            it,
            it1
          )
        }
      }

      notifyChange()
    }
  }


  fun back(v: View) {
    v.findNavController().popBackStack()
  }

  fun share(v: AppCompatImageView) {
    Log.d(TAG, "share: ==========================")
//    val name  = (store.get()?.name != null? "" : "")
    storeUseCase.share(ShareRequest(store.get()?.id))

//    var name = ""
//    store.get()?.name?.let {
//      name = it
//    }
//    var description = ""
//    store.get()?.description?.let {
//      description = it
//    }
//    val name = if(store.get()?.name == null) "" else store.get()?.name
    store.get()?.share?.let { share(v.context, store.get()?.name + store.get()?.description, it) }
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
    store.get()?.phone?.let { callPhone(v.context, it) }
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
    adsAdapter.differ.submitList(data.advertisements)
    propertiesAdapter.selected = 0
    propertiesAdapter.differ.submitList(data.category)
    exploreAdapter.differ.submitList(data.explore)
//    store.get()?.explore?.let { exploreAdapter.differ.currentList.addAll(it) }
//    notifyPropertyChanged(BR.exploreAdapter)
//    exploreAdapter.notifyDataSetChanged()

    store.get()?.socialMediaLinks?.forEach { socialLink ->
      if (socialLink.link.isEmpty()) {
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

  fun storeDetails(v: View) {

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