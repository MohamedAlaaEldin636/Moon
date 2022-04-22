package grand.app.moon.presentation.ads.viewModels

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

  fun share(v: AppCompatImageView){
    advertisement.get()?.title?.let { advertisement.get()?.description?.let { it1 -> share(v.context, it, it1,v) } }
  }

  fun whatsapp(v: View) {
    advertisement.get()?.title?.let {
      advertisement.get()?.description?.let { it1 ->
        advertisement.get()?.store?.phone?.let { it2 ->
          shareWhatsapp(
            v,
            it, it1, it2
          )
        }
      }
    }
  }

  fun phone(v: View) {
    advertisement.get()?.store?.phone?.let { callPhone(v.context, it) }
  }

  fun chat(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      advertisement.get()?.store?.let {
        startChatConversation(v, it.nickname, it.name, it.image)
      }
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

  fun showAllReviews(v: View) {
    if (isLoggin) {
      v.findNavController().navigate(
        AdsDetailsFragmentDirections.actionAdsDetailsFragmentToReviewsFragment(
          advertisement.get()!!.id,
          advertisement.get()!!.averageRate
        )
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
      getDetails(id, type)
    }
  }

  fun updateReviews(result: ReviewsPaginateData) {
    advertisement.get()!!.reviews.addAll(result.list)
    reviewsAdapter.differ.submitList(result.list)
    notifyPropertyChanged(BR.reviewsAdapter)
  }
}