package grand.app.moon.presentation.myStore.viewModel

import androidx.core.text.set
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemExploreInShopInfoBinding
import grand.app.moon.domain.shop.ItemExploreInShopInfo
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideWithDefaultsPlaceholderAndError
import grand.app.moon.extensions.bindingAdapter.setupWithGlideWithDefaultsPlaceholderAndErrorListImagesOrVideo
import grand.app.moon.extensions.bindingAdapter.setupWithGlideWithDefaultsPlaceholderAndErrorVideo
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.myStore.ExploreInShopInfoFragment
import grand.app.moon.presentation.myStore.StoreClientsReviewsFragment
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ExploreInShopInfoViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	/** Day / Month / Year -> 3 / 11 / 2023 */
	val dateFrom = MutableLiveData("")

	val dateTo = MutableLiveData("")

	private val remainingExploreCount = MutableLiveData<Int?>()
	val textRemainingExploreCount = remainingExploreCount.map {
		"${app.getString(R.string.rem_explore_count)} ( ${it.toStringOrEmpty()} )"
	}

	val key = MutableLiveData(1)

	val explores = key.switchMap {
		repoShop.getExplores(dateFrom.value, dateTo.value).asLiveData()
	}.asFlow()

	val adapter = RVPagingItemCommonListUsage<ItemExploreInShopInfoBinding, Pair<Int?, ItemExploreInShopInfo>>(
		R.layout.item_explore_in_shop_info,
		additionalListenersSetups = { adapter, binding ->
			binding.imageView.setOnClickListener { view ->
				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ItemExploreInShopInfo>() ?: return@setOnClickListener

				view.findFragmentOrNull<ExploreInShopInfoFragment>()?.findNavController()?.navigateDeepLinkWithoutOptions(
					"fragment-dest",
					"grand.app.moon.dest.show.images.or.video",
					paths = arrayOf(item.isVideo.not().toString(), item.files.toJsonInlinedOrNull().orEmpty())
				)
			}

			binding.likeValueTextView.setOnClickListener {
				General.TODO("Will be programmed in next sprint isa.")
			}

			binding.commentsValueTextView.setOnClickListener {
				General.TODO("Will be programmed in next sprint isa.")
			}

			binding.sharesValueTextView.setOnClickListener {
				General.TODO("Will be programmed in next sprint isa.")
			}

			binding.delView.setOnClickListener { view ->
				val fragment = view.findFragmentOrNull<ExploreInShopInfoFragment>() ?: return@setOnClickListener

				val context = fragment.context ?: return@setOnClickListener

				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ItemExploreInShopInfo>() ?: return@setOnClickListener

				fragment.showCustomYesNoWarningDialog(
					context.getString(R.string.confirm_deletion),
					context.getString(R.string.del_sub_category_desc)
				) { dialog ->
					fragment.handleRetryAbleActionCancellableNullable(
						action = {
							repoShop.deleteExplore(item.id.orZero())
						}
					) {
						dialog.dismiss()

						fragment.showMessage(context.getString(R.string.done_successfully))

						adapter.refresh()
					}
				}
			}
		}
	) { binding, _, item ->
		if (remainingExploreCount.value != item.first) {
			remainingExploreCount.value = item.first
		}

		val context = binding.root.context ?: return@RVPagingItemCommonListUsage

		val explore = item.second

		binding.constraintLayout.tag = explore.toJsonInlinedOrNull()

		if (explore.isVideo) {
			binding.imageView.setupWithGlideWithDefaultsPlaceholderAndErrorVideo(explore.files?.firstOrNull())
		}else {
			binding.imageView.setupWithGlideWithDefaultsPlaceholderAndError(explore.files?.firstOrNull())
		}

		binding.likeValueTextView.text = getSpannedString(
			explore.likesCount.toStringOrEmpty(),
			context.getString(R.string.like)
		)

		binding.commentsValueTextView.text = getSpannedString(
			explore.commentsCount.toStringOrEmpty(),
			context.getString(R.string.view_921)
		)

		binding.sharesValueTextView.text = getSpannedString(
			explore.sharesCount.toStringOrEmpty(),
			context.getString(R.string.share)
		)

		binding.creationDateValueTextView.text = explore.createdAt.orEmpty()
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

	fun search() {
		key.value = key.value.orZero().inc()
	}

	fun goToAddExplore(view: View) {
		view.findFragmentOrNull<ExploreInShopInfoFragment>()?.findNavController()?.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.add.explore"
		)
	}

	private fun getSpannedString(value1: String, value2: String) = buildSpannedString {
		append("$value1 ")

		val start = length
		append(value2)
		this[start..length] = ForegroundColorSpan(Color.BLACK)
	}

}
