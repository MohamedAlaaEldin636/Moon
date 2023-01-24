package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreClientsReviewsBinding
import grand.app.moon.domain.shop.ResponseClientReviews
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrElseResNameBA
import grand.app.moon.presentation.myStore.StoreClientsReviewsFragment
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class StoreClientsReviewsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop
) : AndroidViewModel(application) {

	val name = MutableLiveData("")

	/** Day / Month / Year -> 3 / 11 / 2023 */
	val dateFrom = MutableLiveData("")

	val dateTo = MutableLiveData("")

	val adapter = RVPagingItemCommonListUsage<ItemStoreClientsReviewsBinding, ResponseClientReviews>(
		R.layout.item_store_clients_reviews,
		additionalListenersSetups = { adapter, binding ->
			binding.whatsAppImageView.setOnClickListener { view ->
				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ResponseClientReviews>() ?: return@setOnClickListener

				view.context?.launchWhatsApp(item.user?.phone.orEmpty())
			}
		}
	) { binding, position, item ->
		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		binding.nameTextView.text = item.user?.name.orEmpty()
		binding.emailTextView.text = item.user?.email.orEmpty()
		binding.phoneTextView.text = item.user?.phone.orEmpty()

		binding.imageView.setupWithGlideOrElseResNameBA(
			item.user?.image.orEmpty(), "ic_default_user"
		)

		binding.dateTextView.text = item.date.orEmpty()

		binding.ratingBar.setProgressBA(item.rate.orZero() * 20)

		binding.commentTextView.text = item.review.orEmpty()
	}

	fun pickDate(view: View, isFromNotTo: Boolean) {
		val fragment = view.findFragmentOrNull<StoreClientsReviewsFragment>() ?: return

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
		val fragment = view.findFragmentOrNull<StoreClientsReviewsFragment>() ?: return

		fragment.retryAbleFlow.retry()
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
