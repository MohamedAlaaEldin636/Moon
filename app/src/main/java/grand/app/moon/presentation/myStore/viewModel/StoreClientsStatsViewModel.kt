package grand.app.moon.presentation.myStore.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemAdvInStoreStatsBinding
import grand.app.moon.databinding.ItemSliderStatsChartBinding
import grand.app.moon.databinding.ItemStatsInAdvDetailsBinding
import grand.app.moon.domain.stats.ChartData
import grand.app.moon.domain.stats.toChartData
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrEmptyBA
import grand.app.moon.presentation.myAds.MyAdsFragment
import grand.app.moon.presentation.myAds.model.ItemStatsInAdvDetails
import grand.app.moon.presentation.myAds.model.ResponseMyAdvDetails
import grand.app.moon.presentation.myStore.ExploreInShopInfoFragment
import grand.app.moon.presentation.myStore.RVSliderStoreClientsStats2
import grand.app.moon.presentation.myStore.StoreClientsStatsFragment
import grand.app.moon.presentation.stats.models.ItemStoreStats
import grand.app.moon.presentation.stats.models.ResponseGeneralStats
import grand.app.moon.presentation.stats.viewModels.ItemStatsChartViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class StoreClientsStatsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	/** Day / Month / Year -> 3 / 11 / 2023 */
	val dateFrom = MutableLiveData("")

	val dateTo = MutableLiveData("")

	/** Used to keep recycled pages having it's own last changes data isa. */
	//val map = mutableMapOf<ItemStoreStats.Type, ChartData>()

	private val drawablePositiveGrowth by lazy {
		ContextCompat.getDrawable(app, R.drawable.ic_positive_growth).orTransparent()
	}
	private val drawableNegativeGrowth by lazy {
		ContextCompat.getDrawable(app, R.drawable.ic_negative_growth).orTransparent()
	}

	val adapterSliderView = RVSliderStoreClientsStats2()

	val adapterStats = RVItemCommonListUsage<ItemStatsInAdvDetailsBinding, ItemStoreStats>(
		R.layout.item_stats_in_adv_details,
		emptyList(),
		onItemClick = { _, binding ->
			val context = binding.root.context ?: return@RVItemCommonListUsage

			val item = (binding.constraintLayout.tag as? String).fromJsonInlinedOrNull<ItemStoreStats>()
				?: return@RVItemCommonListUsage

			val type = item.type ?: return@RVItemCommonListUsage

			val titlePlural = context.getString(type.titlePluralRes)
			val titleSingular = context.getString(type.titleSingularRes)

			when (type) {
				ItemStoreStats.Type.ADVERTISEMENTS -> {
					binding.root.findNavController().navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.my.ads.with.stats",
						paths = arrayOf(titlePlural, titleSingular, MyAdsFragment.InitialFilter.RECENTLY_ADDED.toString())
					)
				}
				ItemStoreStats.Type.EXPLORES -> {
					binding.root.findNavController().navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.explore.in.shop.info.with.stats",
						paths = arrayOf(titlePlural, titleSingular)
					)
				}
				ItemStoreStats.Type.STORIES -> {
					binding.root.findNavController().navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.story.in.shop.info.with.stats",
						paths = arrayOf(titlePlural, titleSingular)
					)
				}
				else -> {
					binding.root.findNavController().navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.general.stats.store",
						paths = arrayOf(titlePlural, titleSingular, type.toString(), false.toString())
					)
				}
			}
		}
	) { binding, _, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		binding.titleTextView.text = item.name

		binding.numberTextView.text = item.totalCount.orZero().toString()

		binding.statsTextView.text = buildSpannedString {
			val growthRate = item.growthRate
			val isPositive = growthRate >= 0.toBigDecimal()
			val absGrowthRate = growthRate.abs()

			append("$absGrowthRate  ?  % ")

			this[" ? "] = DrawableSpanKt(
				if (isPositive || absGrowthRate == BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) drawablePositiveGrowth else drawableNegativeGrowth,
			)

			append(context.getString(R.string.comparedd_to_last_month))
		}

		val drawableRes = when (item.type) {
			ItemStoreStats.Type.VIEWS -> R.drawable.store_views
			ItemStoreStats.Type.SHARES -> R.drawable.store_shares
			ItemStoreStats.Type.CALLS -> R.drawable.ic_calls_stats
			ItemStoreStats.Type.CHATS -> R.drawable.ic_chats_stats
			ItemStoreStats.Type.WHATSAPP -> R.drawable.ic_whatsapp_stats
			ItemStoreStats.Type.AD_SEARCHES -> R.drawable.ic_searches_stats
			ItemStoreStats.Type.REPORTS -> R.drawable.ic_reports_stats
			ItemStoreStats.Type.FOLLLOWERS -> R.drawable.store_followers
			ItemStoreStats.Type.ADVERTISEMENTS -> R.drawable.store_ads
			ItemStoreStats.Type.STORIES -> R.drawable.store_story
			ItemStoreStats.Type.BLOCKS -> R.drawable.store_ban
			ItemStoreStats.Type.EXPLORES -> R.drawable.store_explore
			ItemStoreStats.Type.AD_SHARES -> R.drawable.ic_shares_stats
			ItemStoreStats.Type.AD_VIEWS -> R.drawable.ic_views_stats
			ItemStoreStats.Type.AD_REPORTS -> R.drawable.ic_reports_stats
			ItemStoreStats.Type.AD_FAVORITES -> R.drawable.ic_favs_stats
			null -> return@RVItemCommonListUsage
		}
		binding.iconImageView.setImageResource(drawableRes)
	}

	val adapterViewPager = RVItemCommonListUsage<ItemSliderStatsChartBinding, ItemStoreStats>(
		R.layout.item_slider_stats_chart,
	) { binding, _, item ->
		binding.frameLayout.tag = item.toJsonInlinedOrNull()
	}

	val adapterLatestSpecialAds = AdsAdapter()
	val adapterHighestViewedAds = AdsAdapter()
	val adapterRecentlyAddedAds = AdsAdapter()

	val showLatestSpecialAds = MutableLiveData(true)
	val showHighestViewedAds = MutableLiveData(true)
	val showRecentlyAddedAds = MutableLiveData(true)

	fun pickDate(view: View, isFromNotTo: Boolean) {
		val fragment = view.findFragmentOrNull<StoreClientsStatsFragment>() ?: return

		val localDateTimeConstraint = when {
			isFromNotTo && dateTo.value.isNullOrEmpty().not() -> {
				dateTo.value?.let {
					val array = if (it.contains(" / ")) it.split(" / ") else return@let null
					val day = array.getOrNull(0)?.toIntOrNull() ?: return@let null
					val month = array.getOrNull(1)?.toIntOrNull() ?: return@let null
					val year = array.getOrNull(2)?.toIntOrNull() ?: return@let null

					LocalDateTime.of(year, month, day, 0, 0)
				}
			}
			isFromNotTo.not() && dateFrom.value.isNullOrEmpty().not() -> {
				dateFrom.value?.let {
					val array = if (it.contains(" / ")) it.split(" / ") else return@let null
					val day = array.getOrNull(0)?.toIntOrNull() ?: return@let null
					val month = array.getOrNull(1)?.toIntOrNull() ?: return@let null
					val year = array.getOrNull(2)?.toIntOrNull() ?: return@let null

					LocalDateTime.of(year, month, day, 0, 0)
				}
			}
			else -> null
		}
		val millis = localDateTimeConstraint?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
		val constraints = millis?.let {
			CalendarConstraints.Builder().let {
				if (isFromNotTo) it.setEnd(millis) else it.setStart(millis)
			}
		}?.build()

		val picker = MaterialDatePicker.Builder.datePicker()
			.setTitleText(
				if (isFromNotTo) R.string.date_from else R.string.date_to
			)
			.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
			.let {
				if (constraints == null) it else it.setCalendarConstraints(constraints)
			}
			.build()

		picker.addOnNegativeButtonClickListener { picker.dismiss() }

		picker.addOnPositiveButtonClickListener {
			picker.dismiss()

			val localDateTime = LocalDateTime.ofInstant(
				Instant.ofEpochMilli(it ?: return@addOnPositiveButtonClickListener),
				ZoneId.systemDefault()
			)

			val date = "${localDateTime.dayOfMonth} / ${localDateTime.monthValue} / ${localDateTime.year}"
			if (isFromNotTo) {
				dateFrom.value = date
			}else {
				dateTo.value = date
			}
		}

		picker.show(fragment.childFragmentManager, "Date")
	}

	fun search(view: View) {
		val fragment = view.findFragmentOrNull<StoreClientsStatsFragment>() ?: return

		fragment.handleRetryAbleActionOrGoBack(
			hideLoadingCode = {},
			action = {
				repoShop.getStoreStats(
					dateFrom.value?.fromUiToApiDate(),
					dateTo.value?.fromUiToApiDate(),
				)
			}
		) { response ->
			adapterViewPager.submitList(response.statistics.orEmpty())
			adapterSliderView.changeSize(response.statistics?.size.orZero())
			fragment.binding.sliderView.post {
				fragment.binding.sliderView.setSliderAdapter(adapterSliderView)
				//fragment.binding.sliderView.startAutoCycle()
			}

			adapterStats.submitList(response.statistics.orEmpty())

			showLatestSpecialAds.value = response.recentPremiumAds.isNullOrEmpty().not()
			showHighestViewedAds.value = response.mostViewedAds.isNullOrEmpty().not()
			showRecentlyAddedAds.value = response.recentlyAddedAds.isNullOrEmpty().not()

			adapterLatestSpecialAds.submitList(response.recentPremiumAds.orEmpty().take(4))
			adapterHighestViewedAds.submitList(response.mostViewedAds.orEmpty().take(4))
			adapterRecentlyAddedAds.submitList(response.recentlyAddedAds.orEmpty().take(4))

			fragment.binding.recyclerViewStats.postDelayed(500) {
				kotlin.runCatching { fragment.updateChart(fragment.binding.viewPager2.currentItem) }
				kotlin.runCatching { fragment.hideLoading() }
			}
		}
	}

	fun goToShowAllLatestSpecialAds(view: View) {
		val defValueString = app.getString(R.string.def_value_string)

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.my.ads.with.stats",
			paths = arrayOf(defValueString, defValueString, MyAdsFragment.InitialFilter.LATEST_SPECIAL.toString())
		)
	}

	fun goToShowAllHighestViewedAds(view: View) {
		val defValueString = app.getString(R.string.def_value_string)

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.my.ads.with.stats",
			paths = arrayOf(defValueString, defValueString, MyAdsFragment.InitialFilter.HIGHEST_VIEWED.toString())
		)
	}

	fun goToShowAllRecentlyAdded(view: View) {
		val defValueString = app.getString(R.string.def_value_string)

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.my.ads.with.stats",
			paths = arrayOf(defValueString, defValueString, MyAdsFragment.InitialFilter.RECENTLY_ADDED.toString())
		)
	}

	private fun String.fromUiToApiDate(): String? {
		val array = if (contains(" / ")) split(" / ") else return null

		val day = array.getOrNull(0)?.toIntOrNull() ?: return null
		val month = array.getOrNull(1)?.toIntOrNull() ?: return null
		val year = array.getOrNull(2)?.toIntOrNull() ?: return null

		return "${year.toString().minLengthZerosPrefix(4)}-" +
			"${month.toString().minLengthZerosPrefix(2)}-" +
			day.toString().minLengthZerosPrefix(2)
	}

	class AdsAdapter : RVItemCommonListUsage<ItemAdvInStoreStatsBinding, ResponseMyAdvDetails>(
		R.layout.item_adv_in_store_stats,
		onItemClick = { _, binding ->
			binding.apply {
				val id = binding.constraintLayout.tag as? Int ?: return@apply

				binding.root.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
					paths = arrayOf(id.toString())
				)
			}
		},
		onBind = { binding, _, item ->
			binding.apply {
				val context = binding.root.context ?: return@apply

				binding.constraintLayout.tag = item.id

				binding.shapeableImageView.setupWithGlideOrEmptyBA(item.image)

				binding.titleTextView.text = item.title.orEmpty()

				binding.priceTextView.text = buildSpannedString {
					append("${context.getString(R.string.price_8789)} ")

					this[0..length] = ForegroundColorSpan(ContextCompat.getColor(context, R.color.prefix_in_my_ads_item))

					append("${item.price.orZero()} ${item.country?.currency.orEmpty()}")
				}

				binding.categoryTextView.text = buildSpannedString {
					append("${context.getString(R.string.category_8789)} ")

					this[0..length] = ForegroundColorSpan(ContextCompat.getColor(context, R.color.prefix_in_my_ads_item))

					append(item.category?.name.orEmpty())
				}

				binding.premiumImageView.visibility = if (item.isPremium) View.VISIBLE else View.INVISIBLE

				binding.createdAtTextView.text = item.createdAt.orEmpty()
			}
		}
	)

}