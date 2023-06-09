package grand.app.moon.presentation.ads.viewModels

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import grand.app.moon.presentation.reviews.adapters.ReviewsAdapter
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.AdsDetailsFragmentDirections
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.ads.adapter.PropertyAdapter
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import androidx.core.os.bundleOf
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.core.extenstions.*
import grand.app.moon.domain.store.entity.ShareRequest
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.ads.adapter.SwitchAdapter
import java.util.*


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
  var switchAdapter = SwitchAdapter()

  @Bindable
  var reviewsAdapter = ReviewsAdapter()

  @Inject
  @Bindable
  lateinit var adsAdapter: AdsAdapter

  var blockStore = false

  val followStoreRequest = FollowStoreRequest()
  val addFavouriteAdsRequest = AddFavouriteAdsRequest()

  val _adsDetailsResponse =
    MutableStateFlow<Resource<BaseResponse<Advertisement>>>(Resource.Default)
  val adsDetailsResponse = _adsDetailsResponse

  @Bindable
  val advertisement = ObservableField<Advertisement>()
  var isLoggin = userLocalUseCase.isLoggin()

  var fromStore = false

  fun getDetails(id: Int, type: Int, fromStore: Boolean) {
    this.fromStore = fromStore
    Log.d(TAG, "getDetails: ")
    this.id = id
    this.type = type
    useCase.getDetails(id, type)
      .onEach {
        _adsDetailsResponse.value = it
      }.launchIn(viewModelScope)
  }

  fun follow(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      followStoreRequest.storeId = advertisement.get()?.store?.id
      storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
      advertisement.get()?.store?.isFollowing = advertisement.get()?.store?.isFollowing != true
      advertisement.get()?.store?.id?.let {
        advertisement.get()?.store?.isFollowing?.let { it1 ->
          ListHelper.addFollowStore(
            it,
            it1
          )
        }
      }
      notifyChange()
    }
  }

  fun storeDetails(v: View) {
    if (!fromStore) {
      v.findNavController().navigate(
        R.id.nav_store,
        bundleOf(
          "id" to advertisement.get()?.store?.id,
          "type" to 3
        ), Constants.NAVIGATION_OPTIONS
      )

    } else v.findNavController().navigateUp()
  }

  fun back(v: View) {
    v.findNavController().popBackStack()
  }

  fun share(v: View) {
    advertisement.get()?.description?.let {
      advertisement.get()?.share?.let { it1 ->
        share(
          v.context,
          it,
          it1
        )
//        ListHelper.addOrUpdate(advertisement.get()!!)
        if (v.context.isLogin()) {
          advertisement.get()!!.shareCount++
          notifyPropertyChanged(BR.advertisement)
//          storeUseCase.share()
          storeUseCase.share(ShareRequest(advertisement.get()?.id)).onEach {
          }.launchIn(viewModelScope)
        }
      }
    }
  }

  fun whatsapp(v: View) {
//    shareWhatsapp(
//      v,advertisement.get()!!.share,null,advertisement.get()!!.phone
//    )
    advertisement.get()?.title?.let {
      advertisement.get()?.description?.let { it1 ->
        advertisement.get()?.phone?.let { it2 ->
          shareWhatsapp(
            v,
            it, it1, advertisement.get()?.country?.country_code+it2
          )
        }
      }
    }
  }

  fun location(v: View) {
    advertisement.get()?.longitude?.let {
      advertisement.get()?.latitude?.let { it1 ->
        v.context.navigateMap(
          it1, it
        )
      }
    }
  }

  fun phone(v: View) {
    advertisement.get()?.phone?.let { callPhone(v.context, advertisement.get()?.country?.country_code+it) }
  }

  fun chat(v: View) {
    if (v.context.isLoginWithOpenAuth()) {
      if(v.isChatAllow()) {
        advertisement.get()?.store?.let {
          v.context.openChatStore(v, it.id.orZero(), it.name.orEmpty(), it.image)
        }
      }
    }
  }

  fun favourite(v: View) {

    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      addFavouriteAdsRequest.advertisementId = advertisement.get()?.id
      useCase.favourite(addFavouriteAdsRequest).launchIn(viewModelScope)
      advertisement.get()?.isFavorite = advertisement.get()?.isFavorite != true
      when (advertisement.get()!!.isFavorite) {
        true -> advertisement.get()!!.favoriteCount++
        else -> advertisement.get()!!.favoriteCount--
      }
      Log.d(TAG, "favourite: ${advertisement.get()!!.favoriteCount}")
      ListHelper.addOrUpdate(advertisement.get()!!)
      notifyChange()
    }
  }

  fun update(data: Advertisement) {
    advertisement.set(data)
    ListHelper.addOrUpdate(advertisement.get()!!)
    propertiesAdapter.differ.submitList(advertisement.get()?.properties)
    reviewsAdapter.differ.submitList(advertisement.get()?.reviews)
    adsAdapter.differ.submitList(advertisement.get()?.similarAds)
    switchAdapter.differ.submitList(advertisement.get()?.switches)
    notifyPropertyChanged(BR.switchAdapter)
    show.set(true)
  }

  fun showAllReviews(v: View) {
    if (isLoggin) {
      v.findNavController().navigate(
        AdsDetailsFragmentDirections.actionAdsDetailsFragmentToReviewsFragment
          (advertisement.get()!!.id, advertisement.get()!!.averageRate, -1)
      )

    } else
      clickEvent.value = Constants.LOGIN_REQUIRED
  }

  private val TAG = "AdsDetailsViewModel"
  fun recallApi(isAuthorize: Boolean) {
    Log.d(TAG, "recallApi: recallApi recallApi")
    if (!isLoggin && isAuthorize) {
      Log.d(TAG, "recallApi: DONER")
      isLoggin = isAuthorize
      getDetails(id, type, fromStore)
    }
  }

  fun updateReviews(result: ReviewsPaginateData) {
    advertisement.get()!!.reviews.addAll(result.list)
    reviewsAdapter.differ.submitList(result.list)
    notifyPropertyChanged(BR.reviewsAdapter)
  }

  fun reportAds(v: View) {
    if (isLoggin) {
      v.findNavController().navigate(
        AdsDetailsFragmentDirections.actionAdsDetailsFragmentToReportDialog(
          v.resources.getString(
            R.string.please_choose_reason_for_report
          ), 6, advertisement.get()?.id!!,
          -1
        )
      )
    } else
      clickEvent.value = Constants.LOGIN_REQUIRED
  }

  fun checkBlockStore(): Boolean {
    if (advertisement.get()?.id != 0) {
      if (advertisement.get()?.store?.id?.let { ListHelper.checkBlockStore(it) } == true)
        return true
    }
    return false
  }
}