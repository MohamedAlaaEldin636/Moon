package grand.app.moon.presentation.myAds.viewModel

import androidx.core.text.set
import android.app.Application
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
import grand.app.moon.presentation.base.extensions.showPopup
import grand.app.moon.presentation.myAds.MyAdsFragment
import grand.app.moon.presentation.myAds.model.MyAdsFilter
import grand.app.moon.presentation.myAds.model.ResponseMyAdvDetails
import grand.app.moon.presentation.myAds.model.TypeOfAd
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MyAdsViewModel @Inject constructor(
	application: Application,
	private val adsUseCase: AdsUseCase,
) : AndroidViewModel(application) {

	val title = MutableLiveData("")
	private val typeOfAd = MutableLiveData(TypeOfAd.ALL)
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

		binding.ratingTextView.text = "( ${item.averageRate?.round(1)
			?.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

		binding.favsTextView.text = item.favoriteCount.toStringOrEmpty()

		binding.viewsTextView.text = item.viewsCount.toStringOrEmpty()

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
		/*filter.value = MyAdsFilter(
			title.value.orEmpty(),
			typeOfAd.value,
			dateFrom.value?.fromUiToApiDate(),
			dateTo.value?.fromUiToApiDate(),
		)*/

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
				)
			}
		) { response ->
			showRecyclerViewNotEmptyView.value = response.advertisements.isNullOrEmpty().not()
			adapter.submitList(response.advertisements.orEmpty())
		}
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
