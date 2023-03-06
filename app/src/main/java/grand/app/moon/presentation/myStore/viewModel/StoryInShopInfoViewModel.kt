package grand.app.moon.presentation.myStore.viewModel

import androidx.core.text.set
import android.app.Application
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoryInShopInfoBinding
import grand.app.moon.domain.shop.ItemExploreInShopInfo
import grand.app.moon.domain.shop.ItemStoryInShopInfo
import grand.app.moon.domain.shop.StoryType
import grand.app.moon.domain.stats.toChartData
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragment
import grand.app.moon.presentation.myStore.StoryInShopInfoFragment
import grand.app.moon.presentation.myStore.StoryInShopInfoFragmentArgs
import grand.app.moon.presentation.stats.models.ResponseGeneralStats
import grand.app.moon.presentation.stats.viewModels.ItemStatsChartViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class StoryInShopInfoViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: StoryInShopInfoFragmentArgs,
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

	val type = MutableLiveData<StoryType?>()

	val typeValue = type.map { type ->
		type?.let { app.getString(it.stringRes) }.orEmpty()
	}

	/** Day / Month / Year -> 3 / 11 / 2023 */
	val dateFrom = MutableLiveData("")

	val dateTo = MutableLiveData("")

	val remainingCount = MutableLiveData<Int?>()
	val textRemainingCount = remainingCount.map {
		"${app.getString(R.string.rem_story_count)} ( ${it.toStringOrEmpty()} )"
	}

	val adapter = RVPagingItemCommonListUsage<ItemStoryInShopInfoBinding, ItemStoryInShopInfo>(
		R.layout.item_story_in_shop_info,
		additionalListenersSetups = { adapter, binding ->
			binding.imageView.setOnClickListener { view ->
				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ItemStoryInShopInfo>() ?: return@setOnClickListener

				view.findFragmentOrNull<StoryInShopInfoFragment>()?.findNavController()?.navigateDeepLinkWithoutOptions(
					"fragment-dest",
					"grand.app.moon.dest.show.images.or.video",
					paths = arrayOf(item.isVideo.not().toString(), listOf(item.file).toJsonInlinedOrNull().orEmpty())
				)
			}

			binding.likeValueTextView.setOnClickListener { view ->
				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ItemStoryInShopInfo>() ?: return@setOnClickListener

				SimpleUserListOfInteractionsFragment.launch(
					view.findNavController(),
					"الإعجابات",
					"",
					SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_LIKES,
					item.id.orZero()
				)
			}

			binding.commentsValueTextView.setOnClickListener { view ->
				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ItemStoryInShopInfo>() ?: return@setOnClickListener

				SimpleUserListOfInteractionsFragment.launch(
					view.findNavController(),
					"المشاهدات",
					"",
					SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_VIEWS,
					item.id.orZero()
				)
			}

			binding.sharesValueTextView.setOnClickListener { view ->
				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ItemStoryInShopInfo>() ?: return@setOnClickListener

				SimpleUserListOfInteractionsFragment.launch(
					view.findNavController(),
					"المشاركات",
					"",
					SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_SHARES,
					item.id.orZero()
				)
			}

			binding.delView.setOnClickListener { view ->
				val fragment = view.findFragmentOrNull<StoryInShopInfoFragment>() ?: return@setOnClickListener

				val context = fragment.context ?: return@setOnClickListener

				val item = (binding.constraintLayout.tag as? String)
					.fromJsonInlinedOrNull<ItemStoryInShopInfo>() ?: return@setOnClickListener

				fragment.showCustomYesNoWarningDialog(
					context.getString(R.string.confirm_deletion),
					context.getString(R.string.del_story_desc)
				) { dialog ->
					fragment.handleRetryAbleActionCancellableNullable(
						action = {
							repoShop.deleteStory(item.id.orZero())
						}
					) {
						dialog.dismiss()

						fragment.showMessage(context.getString(R.string.done_successfully))

						remainingCount.value = remainingCount.value.orZero().inc()

						fragment.retryAbleFlow.retry()
					}
				}
			}
		}
	) { binding, _, story ->
		if (remainingCount.value != story.storiesRestCount) {
			remainingCount.value = story.storiesRestCount
		}

		val context = binding.root.context ?: return@RVPagingItemCommonListUsage

		binding.constraintLayout.tag = story.toJsonInlinedOrNull()

		if (story.isVideo) {
			Glide.with(binding.imageView)
				.load(story.file)
				//.override(imgWidth, imgHeight)
				.apply(RequestOptions().frame(1)/*.centerCrop()*/)
				.placeholder(R.drawable.ic_baseline_refresh_24)
				.error(R.drawable.ic_baseline_broken_image_24)
				.into(binding.imageView)
			//binding.imageView.setupWithGlideWithDefaultsPlaceholderAndErrorVideo(explore.files?.firstOrNull())
		}else {
			Glide.with(binding.imageView)
				.load(story.file)
				//.override(imgWidth, imgHeight)
				.placeholder(R.drawable.ic_baseline_refresh_24)
				.error(R.drawable.ic_baseline_broken_image_24)
				.into(binding.imageView)
			//binding.imageView.setupWithGlideWithDefaultsPlaceholderAndError(explore.files?.firstOrNull())
		}

		binding.likeValueTextView.text = getSpannedString(
			story.likesCount.orZero().toStringOrEmpty(),
			context.getString(R.string.like)
		)

		binding.commentsValueTextView.text = getSpannedString(
			story.viewsCount.orZero().toStringOrEmpty(),
			context.getString(R.string.view_921)
		)

		binding.sharesValueTextView.text = getSpannedString(
			story.sharesCount.orZero().toStringOrEmpty(),
			context.getString(R.string.share)
		)

		binding.creationDateValueTextView.text = story.createdAtDate.orEmpty()

		binding.destructionDateValueTextView.text = "${story.hoursRemaining.orZero()} ${app.getString(R.string.hours)}"
	}

	fun pickStoryType(view: View) {
		view.showPopup(
			StoryType.values().map {
				app.getString(it.stringRes)
			},
			listener = { menuItem ->
				type.value = StoryType.values().firstOrNull {
					app.getString(it.stringRes) == menuItem.title.toStringOrEmpty()
				}
			}
		)
	}

	fun pickDate(view: View, isFromNotTo: Boolean) {
		val fragment = view.findFragmentOrNull<StoryInShopInfoFragment>() ?: return

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
		view.findFragmentOrNull<StoryInShopInfoFragment>()?.retryAbleFlow?.retry()
	}

	fun goToAddStory(view: View) {
		val fragment = view.findFragmentOrNull<StoryInShopInfoFragment>() ?: return

		if (remainingCount.value.orZero() > 0) {
			fragment.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.add.story"
			)
		}else {
			fragment.showError(fragment.getString(R.string.no_more_rem_stories_in_your_package))
		}
	}

	private fun getSpannedString(value1: String, value2: String) = buildSpannedString {
		append("$value1 ")

		val start = length
		append(value2)
		this[start..length] = ForegroundColorSpan(Color.BLACK)
	}

}
