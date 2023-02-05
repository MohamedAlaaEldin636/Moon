package grand.app.moon.presentation.stats.viewModels

import android.app.Application
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemUserInStatsBinding
import grand.app.moon.domain.stats.ChartData
import grand.app.moon.extensions.*
import grand.app.moon.presentation.stats.GeneralStatsFragment
import grand.app.moon.presentation.stats.GeneralStatsFragmentArgs
import grand.app.moon.presentation.stats.models.ResponseUserInGeneralStats
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class GeneralStatsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: GeneralStatsFragmentArgs,
) : AndroidViewModel(application) {

	val data = MutableLiveData(ChartData())

	val name = MutableLiveData("")

	/** Day / Month / Year -> 3 / 11 / 2023 */
	val dateFrom = MutableLiveData("")

	val dateTo = MutableLiveData("")

	val adapter = RVPagingItemCommonListUsage<ItemUserInStatsBinding, ResponseUserInGeneralStats>(
		R.layout.item_user_in_stats,
		onItemClick = { adapter, binding ->
			General.TODO("waiting sth from backend")
		},
		additionalListenersSetups = { _, binding ->
			val item = (binding.constraintLayout.tag as? String).fromJsonInlinedOrNull<ResponseUserInGeneralStats>()
				?: return@RVPagingItemCommonListUsage

			binding.whatsappImageView.setOnClickListener { view ->
				view.context?.launchWhatsApp(item.countryCode.orEmpty() + item.phone.orEmpty())
			}
		}
	) { binding, position, item ->
		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		val count = item.count
		binding.countTextView.isVisible = count != null
		binding.countTextView.text = count.orZero().toString()

		binding.imageImageView.setupWithGlide {
			load(item.image)
				.placeholder(R.drawable.ic_default_user)
		}

		binding.nameTextView.text = item.name
		binding.emailValueTextView.text = item.email
		binding.phoneValueTextView.text = "${item.countryCode} ${item.phone}"

		binding.dateTextView.text = item.createdAt
	}

	fun pickDate(view: View, isFromNotTo: Boolean) {
		val fragment = view.findFragmentOrNull<GeneralStatsFragment>() ?: return

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
		val fragment = view.findFragmentOrNull<GeneralStatsFragment>() ?: return

		fragment.retryAbleFlow.retry()
	}

}
