package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.text.style.StrikethroughSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.launchCometChat
import grand.app.moon.core.extenstions.showError
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemPropertyInAdvDetailsBinding
import grand.app.moon.databinding.ItemReviewInAdvDetailsBinding
import grand.app.moon.databinding.ItemSwitchInAdvDetailsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrElseResNameBA
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrSplashBA
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.OtherAdvDetailsFragment
import grand.app.moon.presentation.home.OtherAdvDetailsFragmentArgs
import grand.app.moon.presentation.home.ReportingDialogFragment
import grand.app.moon.presentation.home.utils.getAdapterForAds
import grand.app.moon.presentation.home.utils.getAdapterForStores
import grand.app.moon.presentation.myAds.adapter.RVSliderImageFull
import grand.app.moon.presentation.myAds.model.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class OtherAdvDetailsViewModel @Inject constructor(
	application: Application,
	val args: OtherAdvDetailsFragmentArgs,
	val adsUseCase: AdsUseCase,
	val homeUseCase: HomeUseCase,
	val userLocalUseCase: UserLocalUseCase,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val response = MutableLiveData<ResponseMyAdvDetails?>()

	val adapterImages = RVSliderImageFull()

	val isFavorite = response.mapToMutableLiveData {
		it?.isFavorite.orFalse()
	}

	/** ( 4.5 ) */
	val ratingText = response.map {
		"( ${it?.averageRate?.round(1).orZero().toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"
	}

	/** 102 */
	val favText = response.map {
		it?.favoriteCount.orZero().toStringOrEmpty()
	}

	/** 102 */
	val viewsText = response.map {
		it?.viewsCount.orZero().toStringOrEmpty()
	}

	/** 102 */
	val sharesText = response.map {
		it?.shareCount.orZero().toStringOrEmpty()
	}

	val showPremium = response.map {
		it?.isPremium.orFalse()
	}

	/**
	 * تم النشر منذ 30 دقيقة
	 */
	val remainingDays = response.map {
		app.getString(R.string.published_var, it?.createdAt.orEmpty())
	}

	val title = response.map {
		it?.title.orEmpty()
	}

	/**
	 * 2000 ريال
	 */
	val price = response.map {
		"${it?.price.orZero()} ${it?.country?.currency.orEmpty()}"
	}

	/** same as [price] but strikethrough */
	val oldPrice: LiveData<CharSequence> = response.map {
		buildSpannedString {
			append("${it?.priceBeforeDiscount.orZero()} ${it?.country?.currency.orEmpty()}")

			this[0..length] = StrikethroughSpan()
		}
	}
	val showOldPrice = response.map {
		it?.priceBeforeDiscount != null
	}

	val showIsNegotiable = response.map {
		it?.isNegotiable.orFalse()
	}

	private val locationData = response.map {
		LocationData(
			it?.latitude ?: return@map null,
			it.longitude ?: return@map null,
			it.address.orEmpty()
		)
	}
	val address = locationData.map {
		if (it?.address.isNullOrEmpty()) {
			val latitude = it?.latitude?.toDoubleOrNull()
			val longitude = it?.longitude?.toDoubleOrNull()
			if (latitude == null || longitude == null) {
				app.getString(R.string.no_address_found)
			}else {
				app.getAddressFromLatitudeAndLongitude(latitude, longitude)
			}
		}else {
			it?.address
		}
	}

	val description = response.map {
		it?.description
	}

	val adapterProperties = RVItemCommonListUsage<ItemPropertyInAdvDetailsBinding, ItemPropertyInAdvDetails>(
		R.layout.item_property_in_adv_details,
		emptyList(),
	) { binding, _, item ->
		// Here only properties without boolean values.

		binding.labelTextView.text = item.parent?.name ?: item.name

		binding.valueTextView.text = if (item.parent == null) item.text else item.name
	}
	val showProperties = response.map {
		it?.properties.isNullOrEmpty().not()
	}

	val adapterSwitches = RVItemCommonListUsage<ItemSwitchInAdvDetailsBinding, ItemPropertyInAdvDetails>(
		R.layout.item_switch_in_adv_details,
		emptyList(),
	) { binding, _, item ->
		// Here only properties of boolean values.

		binding.textView.text = item.name

		binding.imageView.setupWithGlideOrSplashBA(item.image)
	}
	val showSwitches = response.map {
		it?.switches.isNullOrEmpty().not()
	}

	val ownerImage = response.map {
		it?.store?.image.orEmpty()
	}
	val ownerName = response.map {
		it?.store?.name.orEmpty()
	}
	val ownerNickname = response.map {
		it?.store?.nickname.orEmpty()
	}
	val ownerAverageRateText = response.map {
		"( ${it?.store?.averageRate?.round(1).orZero().toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"
	}
	val ownerAverageRateValue = response.map {
		it?.store?.averageRate?.round(1).orZero().times(20f).roundToInt()
	}
	val ownerFollowingTextDrawable = response.map {
		if (it?.store?.isFollowing.orFalse()) {
			null
		}else {
			ContextCompat.getDrawable(app, R.drawable.follow_add)
		}
	}
	val ownerFollowingText = response.map {
		if (it?.store?.isFollowing.orFalse()) {
			app.getString(R.string.un_follow)
		}else {
			app.getString(R.string.follow)
		}
	}

	val adapterReviews = RVItemCommonListUsage<ItemReviewInAdvDetailsBinding, ItemReviewInAdvDetails>(
		R.layout.item_review_in_adv_details,
	) { binding, _, item ->
		binding.nameTextView.text = item.user?.name

		binding.commentTextView.text = item.review

		binding.ratingBar.isVisible = item.user?.id != kotlin.runCatching { userLocalUseCase().id }.getOrNull()
		binding.ratingBar.setProgressBA(item.rate.orZero() * 20)

		binding.imageView.setupWithGlideOrElseResNameBA(item.user?.image, "ic_default_user")
	}

	val showSimilarAds = response.map {
		it?.similarAds.isNullOrEmpty().not()
	}

	val showSimilarStores = response.map {
		it?.similarStores.isNullOrEmpty().not()
	}

	val adapterAds = getAdapterForAds()

	val adapterStores = getAdapterForStores()

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

	fun favOrUnFav(view: View) {
		val context = view.context ?: return

		if (context.isLoginWithOpenAuth()) {
			isFavorite.toggleValue()

			context.applicationScope?.launch {
				repoShop.favOrUnFavAdv(response.value?.id.orZero())
			}
		}
	}

	fun shareThisScreen(view: View) {
		val link = response.value?.shareLink.orEmpty()

		if (link.isEmpty()) return view.context.showError(view.context.getString(R.string.no_link_exists))

		view.context?.applicationScope?.launch {
			repoShop.shareAdv(response.value?.id.orZero())
		}

		view.context.launchShareText(link)
	}

	fun showLocation(view: View) {
		val fragment = view.findFragmentOrNull<OtherAdvDetailsFragment>() ?: return

		val latitude = locationData.value?.latitude?.toDoubleOrNull()?.toString()
		val longitude = locationData.value?.longitude?.toDoubleOrNull()?.toString()

		if (latitude == null || longitude == null) {
			return fragment.showMessage(fragment.getString(R.string.no_address_found))
		}

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.location.viewer",
			paths = arrayOf(latitude, longitude)
		)
	}

	fun followOrUnFollowStore(view: View) {
		val context = view.context ?: return

		if (context.isLoginWithOpenAuth()) {
			response.value = response.value?.copy(
				store = response.value?.store?.copy(
					isFollowing = response.value?.store?.isFollowing.orFalse().not()
				)
			)

			context.applicationScope?.launch {
				repoShop.followStore(response.value?.store?.id.orZero())
			}
		}
	}

	fun goToStore(view: View) {
		userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
			view.context ?: return,
			view.findNavController(),
			response.value?.store
		)
	}

	fun goToReviews(view: View) {
		val fragment = view.findFragmentOrNull<OtherAdvDetailsFragment>() ?: return

		val context = fragment.context ?: return

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.adv.clients.reviews",
			paths = arrayOf(
				response.value?.id.orZero().toString(),
				(context.isLogin() && response.value?.store?.id == userLocalUseCase().id).toString() // useRating
			)
		)
	}

	fun shareLink(view: View, socialMedia: OtherAdvDetailsFragment.SocialMedia) {
		val context = view.context ?: return

		val fragment = view.findFragmentOrNull<OtherAdvDetailsFragment>() ?: return

		val link = response.value?.shareLink.orEmpty()

		if (link.isEmpty()) return fragment.showMessage(context.getString(R.string.no_link_exists))

		when (socialMedia) {
			OtherAdvDetailsFragment.SocialMedia.FACEBOOK -> context.shareOnFacebook(link)
			OtherAdvDetailsFragment.SocialMedia.INSTAGRAM -> context.shareOnInstagram(link)
			OtherAdvDetailsFragment.SocialMedia.TWITTER -> context.shareOnTwitter(link)
			OtherAdvDetailsFragment.SocialMedia.YOUTUBE -> context.shareOnYoutube(link)
			OtherAdvDetailsFragment.SocialMedia.COPY_LINK -> context.copyToClipboard(link)
		}
	}

	fun reportAdv(view: View) {
		ReportingDialogFragment.launch(
			view.findNavController(),
			ReportingDialogFragment.Type.REPORT_ADS,
			response.value?.id.orZero()
		)
	}

	fun goToWhatsApp(view: View) {
		val fragment = view.findFragmentOrNull<OtherAdvDetailsFragment>() ?: return

		val context = fragment.context ?: return

		context.launchWhatsApp(response.value?.fullPhone.orEmpty())
	}

	fun makeCall(view: View) {
		val fragment = view.findFragmentOrNull<OtherAdvDetailsFragment>() ?: return

		val context = fragment.context ?: return

		context.launchDialNumber(response.value?.fullPhone.orEmpty())
	}

	fun chat(view: View) {
		val fragment = view.findFragmentOrNull<OtherAdvDetailsFragment>() ?: return

		val context = fragment.context ?: return

		if (context.isLoginWithOpenAuth()) {
			response.value?.store?.also {
				context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
			}
		}
	}

}
