package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.text.style.StrikethroughSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError
import grand.app.moon.databinding.ItemPropertyInAdvDetailsBinding
import grand.app.moon.databinding.ItemReviewInAdvDetailsBinding
import grand.app.moon.databinding.ItemStatsInAdvDetailsBinding
import grand.app.moon.databinding.ItemSwitchInAdvDetailsBinding
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrSplashBA
import grand.app.moon.presentation.myAds.MyAdvDetailsFragment
import grand.app.moon.presentation.myAds.MyAdvDetailsFragmentArgs
import grand.app.moon.presentation.myAds.MyAdvDetailsFragmentDirections
import grand.app.moon.presentation.myAds.adapter.RVSliderImageFull
import grand.app.moon.presentation.myAds.model.*
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MyAdvDetailsViewModel @Inject constructor(
	application: Application,
	val args: MyAdvDetailsFragmentArgs,
	val adsUseCase: AdsUseCase,
	val homeUseCase: HomeUseCase,
) : AndroidViewModel(application) {

	val response = MutableLiveData<ResponseMyAdvDetails?>(
		args.jsonOfResponseMyAdvDetails.fromJsonInlinedOrNull()
	)

	val adapterImages = RVSliderImageFull()

	/** ( 4.5 ) */
	val ratingText = response.map {
		"( ${it?.averageRate?.round(1)?.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"
	}

	/** 102 */
	val favText = response.map {
		it?.favoriteCount.toStringOrEmpty()
	}

	/** 102 */
	val viewsText = response.map {
		it?.viewsCount.toStringOrEmpty()
	}

	/** 102 */
	val sharesText = response.map {
		it?.shareCount.toStringOrEmpty()
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
			append("${it?.oldPrice.orZero()} ${it?.country?.currency.orEmpty()}")

			this[0..length] = StrikethroughSpan()
		}
	}
	val showOldPrice = response.map {
		it?.oldPrice != null
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
		onItemClick = { _, _ ->
			General.TODO("Will be programmed later as it is only available for store account")
		}
	) { binding, _, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.titleTextView.text = item.name

		binding.numberTextView.text = item.totalCount.orZero().toString()

		binding.statsTextView.text = buildSpannedString {
			val growthRate = item.growthRate
			val isPositive = growthRate > 0.toBigDecimal()
			val absGrowthRate = growthRate.abs()

			append("$absGrowthRate ? % ")

			this["?"] = DrawableSpan(
				if (isPositive || absGrowthRate == BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) drawablePositiveGrowth else drawableNegativeGrowth
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

		binding.ratingBar.setProgressBA(item.rate.orZero() * 20)

		binding.imageView.setupWithGlideOrSplashBA(item.user?.image)
	}
	val showReviews = response.map {
		it?.reviews.isNullOrEmpty().not()
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
		General.TODO("Will be programmed later in another sprint")
	}

	fun editAdv(view: View) {
		TODO()
	}

	/**
	 * - Normal flow is to check available slots for premium ads lets say 0 available,
	 * so Either no package is subscribed or a 1 which is full currently,
	 * So should redirect to packages till subscribe to 1, then come back here and make ad premium,
	 * since we are in the stage where packages not done yet then just make it success ias.
	 */
	fun makeAdvPremiumOrCheckAdvPremium(view: View) {
		if (response.value?.isPremium == true) {
			return General.TODO("Will be programmed later in another sprint")
		}

		val id = response.value?.id ?: return

		val fragment = view.findFragmentOrNull<MyAdvDetailsFragment>() ?: return

		fragment.handleRetryAbleActionCancellable(
			action = {
				homeUseCase.getCheckAvailabilityForPremiumAds()
			}
		) { availableAds ->
			MyLogger.e("availableAds $availableAds")

			if (availableAds > 0) {
				makeAdPremium(id, fragment)
			}else {
				// todo change later to redirect to packages but for now skit it as it is not in this sprint.
				makeAdPremium(id, fragment)
			}
		}
	}

	private fun makeAdPremium(id: Int, fragment: MyAdvDetailsFragment) {
		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				adsUseCase.makeMyAdvertisementPremium(id, 53/*todo forced since for now current sprint not including packages*/)
			}
		) {
			response.value = response.value?.copy()?.also {
				it.makePremium()
			}
		}
	}

	fun deleteAdv(view: View) {
		TODO()
	}

}
