package grand.app.moon.presentation.myAds.viewModel

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
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.core.extenstions.showError
import grand.app.moon.databinding.ItemPropertyInAdvDetailsBinding
import grand.app.moon.databinding.ItemReviewInAdvDetailsBinding
import grand.app.moon.databinding.ItemStatsInAdvDetailsBinding
import grand.app.moon.databinding.ItemSwitchInAdvDetailsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.adjustInsideRV
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrElseResNameBA
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrSplashBA
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.MyAdsFragment
import grand.app.moon.presentation.myAds.MyAdvDetailsFragment
import grand.app.moon.presentation.myAds.MyAdvDetailsFragmentArgs
import grand.app.moon.presentation.myAds.MyAdvDetailsFragmentDirections
import grand.app.moon.presentation.myAds.adapter.RVSliderImageFull
import grand.app.moon.presentation.myAds.model.*
import kotlinx.coroutines.flow.firstOrNull
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MyAdvDetailsViewModel @Inject constructor(
	application: Application,
	val args: MyAdvDetailsFragmentArgs,
	val adsUseCase: AdsUseCase,
	val homeUseCase: HomeUseCase,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val user by lazy {
		userLocalUseCase()
	}

	val userId by lazy {
		user.id
	}

	private val isShop by lazy {
		user.isStore.orFalse()
	}

	val response = MutableLiveData<ResponseMyAdvDetails?>(
		args.jsonOfResponseMyAdvDetails.fromJsonInlinedOrNull()
	)

	val adapterImages = RVSliderImageFull()

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
	 * الوقت المتبقي ١٠ أيام
	 */
	val remainingDays = response.map {
		app.getString(R.string.remaining_time_var_days, it?.restDays.orZero())
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
		it?.priceBeforeDiscount.orZero() > 0//it?.priceBeforeDiscount != null && it?.priceBeforeDiscount.orZero() > 0
	}

	val showIsNegotiable = response.map {
		it?.isNegotiable.orFalse()
	}

	private val drawablePositiveGrowth by lazy {
		ContextCompat.getDrawable(app, R.drawable.ic_positive_growth).orTransparent()
	}
	private val drawableNegativeGrowth by lazy {
		ContextCompat.getDrawable(app, R.drawable.ic_negative_growth).orTransparent()
	}
	val adapterStats = RVItemCommonListUsage<ItemStatsInAdvDetailsBinding, ItemStatsInAdvDetails>(
		R.layout.item_stats_in_adv_details,
		emptyList(),
		onItemClick = { adapter, binding ->
			if (userLocalUseCase().isStore.orFalse().not()) {
				return@RVItemCommonListUsage
			}

			val context = binding.root.context ?: return@RVItemCommonListUsage

			val item = (binding.constraintLayout.tag as? String).fromJsonInlinedOrNull<ItemStatsInAdvDetails>()
				?: return@RVItemCommonListUsage

			val type = item.type ?: return@RVItemCommonListUsage

			val titlePlural = context.getString(type.titlePluralRes)
			val titleSingular = context.getString(type.titleSingularRes)
			binding.root.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.general.stats",
				paths = arrayOf(titlePlural, titleSingular, response.value?.id.orZero().toString(), type.toString())
			)
		}
	) { binding, _, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		binding.titleTextView.text = item.name

		binding.numberTextView.text = item.totalCount.orZero().toString()

		binding.statsTextView.isVisible = isShop
		binding.statsTextView.text = buildSpannedString {
			val growthRate = item.growthRate
			val isPositive = growthRate >= 0.toBigDecimal()
			val absGrowthRate = growthRate.abs()

			append("$absGrowthRate  ?  % ")

			this[" ? "] = DrawableSpanKt(
				if (isPositive || absGrowthRate == BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) drawablePositiveGrowth else drawableNegativeGrowth,
				//makeHeightSameAsWidthIfPossible = true,
			)

			append(context.getString(R.string.comparedd_to_last_month))
		}

		val drawableRes = when (item.type) {
			ItemStatsInAdvDetails.Type.VIEWS -> R.drawable.ic_views_stats
			ItemStatsInAdvDetails.Type.SHARES -> R.drawable.ic_shares_stats
			ItemStatsInAdvDetails.Type.CALLS -> R.drawable.ic_calls_stats
			ItemStatsInAdvDetails.Type.CHATS -> R.drawable.ic_chats_stats
			ItemStatsInAdvDetails.Type.WHATSAPP -> R.drawable.ic_whatsapp_stats
			ItemStatsInAdvDetails.Type.FAVORITES -> R.drawable.ic_favs_stats
			ItemStatsInAdvDetails.Type.SEARCH -> R.drawable.ic_searches_stats
			ItemStatsInAdvDetails.Type.REPORTS -> R.drawable.ic_reports_stats
			null -> return@RVItemCommonListUsage
		}
		binding.iconImageView.setImageResource(drawableRes)
	}

	private val locationData = response.map {
		LocationData(
			it?.latitude ?: return@map null,
			it.longitude ?: return@map null,
			it.address ?: return@map null
		)
	}
	val address = locationData.map {
		it?.address
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

	val adapterReviews = RVItemCommonListUsage<ItemReviewInAdvDetailsBinding, ItemReviewInAdvDetails>(
		R.layout.item_review_in_adv_details,
		emptyList(),
	) { binding, _, item ->
		binding.nameTextView.text = item.user?.name

		binding.commentTextView.text = item.review

		binding.ratingBar.isVisible = item.user?.id != userId
		binding.ratingBar.setProgressBA(item.rate.orZero() * 20)

		binding.imageView.setupWithGlideOrElseResNameBA(item.user?.image, "ic_default_user")
	}

	fun goToHomeScreen(view: View) {
		view.findNavController().navigateUp()
	}

	fun shareThisScreen(view: View) {
		val link = response.value?.shareLink.orEmpty()

		if (link.isEmpty()) return view.context.showError(view.context.getString(R.string.no_link_exists))

		view.context.launchShareText(link)
	}

	fun showLocation(view: View) {
		val latitude = locationData.value?.latitude?.toDoubleOrNull()?.toString() ?: return
		val longitude = locationData.value?.longitude?.toDoubleOrNull()?.toString() ?: return

		view.findNavController().navigate(
			MyAdvDetailsFragmentDirections.actionDestMyAdvDetailsToDestLocationViewer(
				latitude, longitude
			)
		)
	}

	fun goToReviews(view: View) {
		val fragment = view.findFragmentOrNull<MyAdvDetailsFragment>() ?: return

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.adv.clients.reviews",
			paths = arrayOf(
				response.value?.id.orZero().toString(),
				false.toString(), // cam from not MY advertisement meaning not my own adv of my account.
				userLocalUseCase().id.toString()
			)
		)
	}

	fun editAdv(view: View) {
		val response = response.value ?: return

		val fragment = view.findFragmentOrNull<MyAdvDetailsFragment>() ?: return

		fragment.handleRetryAbleActionCancellable(
			action = {
				homeUseCase.getCategoriesSuspend()
			}
		) { list ->
			val categoryId = response.category?.id.orZero()

			val category = list.firstOrNull { it.id == categoryId }
				?: return@handleRetryAbleActionCancellable fragment.showError(fragment.getString(R.string.something_went_wrong_please_try_again))

			view.findNavController().navigate(
				MyAdvDetailsFragmentDirections.actionDestMyAdvDetailsToDestAddAdvFinalPage(
					category.id.orZero(),
					response.subCategory?.id.orZero(),
					category.brands.toJsonInlinedOrNull().orEmpty(),
					response.toJsonInlinedOrNull().orEmpty()
				)
			)
		}
	}

	/**
	 * - Normal flow is to check available slots for premium ads lets say 0 available,
	 * so Either no package is subscribed or a 1 which is full currently,
	 * So should redirect to packages till subscribe to 1, then come back here and make ad premium,
	 * since we are in the stage where packages not done yet then just make it success ias.
	 */
	fun makeAdvPremiumOrCheckAdvPremium(view: View) {
		val fragment = view.findFragmentOrNull<MyAdvDetailsFragment>() ?: return

		if (response.value?.isPremium == true) {
			fragment.findNavController().navigateSafely(
				MyAdvDetailsFragmentDirections.actionDestMyAdvDetailsToDestMyAdOrShopPackage(
					response.value?.id.orZero(), true
				)
			)

			return
		}

		fragment.handleRetryAbleActionCancellable(
			action = {
				homeUseCase.getCheckAvailabilityForPremiumAds()
			}
		) { availableAds ->
			MyLogger.e("availableAds $availableAds")

			if (availableAds > 0) {
				updateAdvertisementToBePremium(fragment)
			}else {
				fragment.findNavController().navigateSafely(
					MyAdvDetailsFragmentDirections.actionDestMyAdvDetailsToDestMakeAdvOrStorePremium(
						response.value?.id.orZero()
					)
				)
			}
		}
	}

	private fun updateAdvertisementToBePremium(fragment: MyAdvDetailsFragment) {
		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				adsUseCase.updateAdvertisementToBePremium(response.value?.id.orZero())
			}
		) {
			afterBecamePremium(fragment)
		}
	}

	fun afterBecamePremium(fragment: MyAdvDetailsFragment) {
		response.value = response.value?.copy()?.also {
			it.makePremium()
		}

		fragment.findNavController().setResultInPreviousBackStackEntrySavedStateHandleViaGson(
			MyAdsFragment.NewAdvertisementChange(
				response.value?.id.orZero(),
				MyAdsFragment.NewAdvertisementState.BECAME_PREMIUM
			)
		)
	}

	fun deleteAdv(view: View) {
		val fragment = view.findFragmentOrNull<MyAdvDetailsFragment>() ?: return
		val context = fragment.context ?: return

		val id = response.value?.id ?: return

		fragment.showCustomYesNoWarningDialog(
			context.getString(R.string.confirm_deletion),
			context.getString(R.string.are_you_sure_del_ad)
		) { dialog ->
			fragment.handleRetryAbleActionCancellableNullable(
				action = {
					adsUseCase.deleteAdvertisement(id)
				}
			) {
				dialog.dismiss()

				fragment.showMessage(context.getString(R.string.done_successfully))

				fragment.findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
					MyAdsFragment.NewAdvertisementChange(
						id,
						MyAdsFragment.NewAdvertisementState.DELETED
					)
				)
			}
		}
	}

}
