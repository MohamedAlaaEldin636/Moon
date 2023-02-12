package grand.app.moon.presentation.myAds.viewModel

import androidx.core.text.set
import android.app.Application
import android.content.res.ColorStateList
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.databinding.ItemInMyAdsBinding
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrEmptyBA
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreCategoryInMyAdsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.domain.shop.ResponseStoreSubCategory
import grand.app.moon.domain.stats.toChartData
import grand.app.moon.presentation.myAds.MyAdsFragment
import grand.app.moon.presentation.myAds.MyAdsFragmentArgs
import grand.app.moon.presentation.myAds.model.ResponseMyAdvDetails
import grand.app.moon.presentation.myAds.model.TypeOfAd
import grand.app.moon.presentation.stats.models.ResponseGeneralStats
import grand.app.moon.presentation.stats.viewModels.ItemStatsChartViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MyAdsViewModel @Inject constructor(
	application: Application,
	private val adsUseCase: AdsUseCase,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
	val args: MyAdsFragmentArgs,
) : ItemStatsChartViewModel(application) {

	val showStats = MutableLiveData(args.titlePlural != app.getString(R.string.def_value_string))

	var response: ResponseGeneralStats? = null

	override fun toggleWeek(view: View) {
		val response = response ?: return
		val oldChart = chart.value ?: return
		chart.value = response.toChartData(
			app,
			args.titlePlural,
			args.titleSingular,
			oldChart.weekName != app.getString(R.string.current_week)
		).copy(
			showSaturdayTooltip = oldChart.showSaturdayTooltip,
			showSundayTooltip = oldChart.showSundayTooltip,
			showMondayTooltip = oldChart.showMondayTooltip,
			showTuesdayTooltip = oldChart.showTuesdayTooltip,
			showWednesdayTooltip = oldChart.showWednesdayTooltip,
			showThursdayTooltip = oldChart.showThursdayTooltip,
			showFridayTooltip = oldChart.showFridayTooltip,
		)
	}

	val allCategories = MutableLiveData<List<IdAndName>?>()
	val allSubCategories = MutableLiveData<List<ResponseStoreSubCategory>?>()

	private val selectedCategory = MutableLiveData<IdAndName?>()
	val shownSubCategories = switchMapMultiple2(allCategories, selectedCategory) {
		val selectedCategory = selectedCategory.value
		val allSubCategories = allSubCategories.value.orEmpty()

		if (selectedCategory == null) allSubCategories else {
			listOf(ResponseStoreSubCategory(null, getString(R.string.all), null)) +
				allSubCategories.filter { it.parentId == selectedCategory.id }
		}
	}
	private val selectedSubCategory = MutableLiveData<ResponseStoreSubCategory?>()

	val additionalFilter = switchMapMultiple2(selectedCategory, selectedSubCategory) {
			selectedCategory.value to selectedSubCategory.value
	}

	val showStoreData = MutableLiveData(false)

	private var currentMyAdsNonCategoriesFilter = emptyList<ResponseMyAdvDetails>()

	val adapterCategories = RVItemCommonListUsage<ItemStoreCategoryInMyAdsBinding, IdAndName>(
		R.layout.item_store_category_in_my_ads,
		onItemClick = { adapter, binding ->
			val item = (binding.root.tag as? String).fromJsonInlinedOrNull<IdAndName>() ?: return@RVItemCommonListUsage
			val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@RVItemCommonListUsage

			val oldSelectionPosition = if (selectedCategory.value == null) 0 else {
				adapter.list.indexOfOrNull(selectedCategory.value)
			}

			val newSelection = if (position == 0) null else item
			if (selectedCategory.value?.id != newSelection?.id) {
				selectedCategory.value = newSelection
				selectedSubCategory.value = null

				adapter.notifyItemsChanged(oldSelectionPosition, position)
			}
		}
	) { binding, position, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.root.tag = item.toJsonInlinedOrNull()
		binding.root.setTag(R.id.position_tag, position)

		binding.textView.text = item.name

		val isSelected = (position == 0 && selectedCategory.value == null)
			|| selectedCategory.value?.id == item.id
		binding.textView.backgroundTintList = ColorStateList.valueOf(
			ContextCompat.getColor(context, if (isSelected) R.color.star_enabled else R.color.colorPrimary)
		)
	}

	val adapterSubCategories = RVItemCommonListUsage<ItemStoreCategoryInMyAdsBinding, ResponseStoreSubCategory>(
		R.layout.item_store_category_in_my_ads,
		onItemClick = { adapter, binding ->
			val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ResponseStoreSubCategory>() ?: return@RVItemCommonListUsage
			val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@RVItemCommonListUsage

			val oldSelectionPosition = if (selectedSubCategory.value == null) 0 else {
				adapter.list.indexOfOrNull(selectedSubCategory.value)
			}

			val newSelection = if (position == 0) null else item
			if (selectedSubCategory.value?.id != newSelection?.id) {
				selectedSubCategory.value = newSelection

				adapter.notifyItemsChanged(oldSelectionPosition, position)
			}
		}
	) { binding, position, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.root.tag = item.toJsonInlinedOrNull()
		binding.root.setTag(R.id.position_tag, position)

		binding.textView.text = item.name

		val isSelected = (position == 0 && selectedSubCategory.value == null)
			|| selectedSubCategory.value?.id == item.id
		binding.textView.backgroundTintList = ColorStateList.valueOf(
			ContextCompat.getColor(context, if (isSelected) R.color.star_enabled else R.color.colorPrimary)
		)
	}

	val title = MutableLiveData("")
	val typeOfAd = MutableLiveData(TypeOfAd.ALL)
	val adType = typeOfAd.map {
		app.getString(it?.stringRes ?: R.string.all_ads_3)
	}

	/** Day / Month / Year -> 3 / 11 / 2023 */
	val dateFrom = MutableLiveData("")

	val dateTo = MutableLiveData("")

	//private val filter = MutableLiveData<MyAdsFilter>()

	val showRecyclerViewNotEmptyView = MutableLiveData(false)

	val adapter = RVItemCommonListUsage<ItemInMyAdsBinding, ResponseMyAdvDetails>(
		R.layout.item_in_my_ads,
		onItemClick = { _, binding ->
			val id = binding.constraintLayout.tag as? Int ?: return@RVItemCommonListUsage

			binding.root.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
				paths = arrayOf(id.toString())
			)
		}
	) { binding, _, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.constraintLayout.tag = item.id

		binding.shapeableImageView.setupWithGlideOrEmptyBA(item.image)

		binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
			.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

		binding.favsTextView.text = item.favoriteCount.orZero().toStringOrEmpty()

		binding.viewsTextView.text = item.viewsCount.orZero().toStringOrEmpty()

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

	fun pickAdType(view: View) {
		val context = view.context ?: return

		view.showPopup(
			TypeOfAd.values().map { context.getString(it.stringRes) },
			listener = { menuItem ->
				val type = TypeOfAd.values().firstOrNull {
					context.getString(it.stringRes) == menuItem.title?.toString().orEmpty()
				} ?: return@showPopup

				typeOfAd.value = type
			}
		)
	}

	fun pickDate(view: View, isFromNotTo: Boolean) {
		val fragment = view.findFragmentOrNull<MyAdsFragment>() ?: return

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

	fun searchNow(view: View) {
		val fragment = view.findFragmentOrNull<MyAdsFragment>() ?: return

		searchNow(fragment)
	}
	fun searchNow(fragment: MyAdsFragment) {
		fragment.handleRetryAbleActionCancellable(
			action = {
				adsUseCase.getMyAdvertisements(
					title.value.orEmpty(),
					typeOfAd.value,
					dateFrom.value?.fromUiToApiDate(),
					dateTo.value?.fromUiToApiDate(),
					args.additionalFilter
				)
			}
		) { response ->
			currentMyAdsNonCategoriesFilter = response.advertisements.orEmpty()
			performAdditionalFiltering(selectedCategory.value, selectedSubCategory.value)
			showRecyclerViewNotEmptyView.value = adapter.list.isNotEmpty()
		}
	}

	fun performAdditionalFiltering(
		selectedCategory: IdAndName?, selectedSubCategory: ResponseStoreSubCategory?
	) {
		val list = if (selectedCategory == null && selectedSubCategory == null) {
			currentMyAdsNonCategoriesFilter
		}else {
			val baseList = if (selectedCategory == null) currentMyAdsNonCategoriesFilter else currentMyAdsNonCategoriesFilter.filter {
				it.storeCategoryId == selectedCategory.id
			}

			if (selectedSubCategory == null) baseList else {
				baseList.filter {
					it.storeSubCategoryId == selectedSubCategory.id
				}
			}
		}

		adapter.submitList(list)
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

}
