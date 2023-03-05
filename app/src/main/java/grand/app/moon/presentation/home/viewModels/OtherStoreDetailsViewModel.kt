package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
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
import grand.app.moon.databinding.*
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.visibleOrInvisible
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.home.models.*
import grand.app.moon.presentation.myAds.model.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherStoreDetailsViewModel @Inject constructor(
	application: Application,
	val args: OtherStoreDetailsFragmentArgs,
	val adsUseCase: AdsUseCase,
	val homeUseCase: HomeUseCase,
	val userLocalUseCase: UserLocalUseCase,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val response = MutableLiveData<ResponseStoreDetails?>(null)

	val backgroundImage = response.map {
		it?.backgroundImage.orEmpty()
	}

	val showPremium = response.map {
		it?.isPremium.orFalse()
	}

	val logoImage = response.map {
		it?.image.orEmpty()
	}
	val name = response.map {
		it?.name.orEmpty()
	}
	val hasOffer = response.map {
		it?.hasOffer.orFalse()
	}
	val isFollowing = response.map {
		it?.isFollowing.orFalse()
	}
	val nickname = response.map {
		it?.nickname.orEmpty()
	}
	val showIsOnline = MutableLiveData(false)
	val createdAt = response.map {
		"${app.getString(R.string.member_since)} ${it?.createdAt.orEmpty()}"
	}
	val averageRateText = response.map {
		"( ${it?.averageRate?.round(1).orZero().toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"
	}
	//appCompatRatingBar_setProgressFloat
	val averageRateProgress = response.map {
		it?.averageRate.orZero() * 20f
	}
	val generalAddress = response.map {
		"${it?.country?.name.orEmpty()} ${app.getString(R.string.slash)} ${it?.city?.name.orEmpty()}"
	}
	val website = response.map {
		it?.website.orEmpty()
	}
	val email = response.map {
		it?.email.orEmpty()
	}
	val viewsCount = response.map {
		it?.viewsCount.toStringOrEmpty()
	}
	val ratingsCount = response.map {
		it?.rateCount.toStringOrEmpty()
	}
	val followersCount = response.map {
		it?.followersCount.toStringOrEmpty()
	}
	val adsCount = response.map {
		it?.advertisementsCount.toStringOrEmpty()
	}

	/*private val locationData = response.map {
		LocationData(
			it?.latitude ?: return@map null,
			it.longitude ?: return@map null,
			""
		)
	}*/

	val adapterHighlights = RVItemCommonListUsage<ItemStoryInStoreDetailsBinding, ResponseStory>(
		R.layout.item_story_in_store_details,
		onItemClick = { adapter, binding ->
			val fragment = binding.root.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return@RVItemCommonListUsage
			val context = fragment.context ?: return@RVItemCommonListUsage

			val position = binding.root.tag as? Int ?: return@RVItemCommonListUsage

			val item = adapter.list[position]
			if (context.isLogin() && item.isSeen.not()) {
				item.stories?.firstOrNull()?.isSeen = true

				if (item.isSeen) {
					val updatedList = adapter.list.toMutableList()
					val updatedItem = updatedList.removeAt(position)
					updatedList += updatedItem

					adapter.submitList(updatedList)
				}
			}

			fragment.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.story.player",
				paths = arrayOf(adapter.list.toJsonInlinedOrNull().orEmpty(), position.toString())
			)
		}
	) { binding, position, item ->
		binding.root.tag = position

		binding.view.visibleOrInvisible(item.isSeen.not())

		binding.imageView.setupWithGlide {
			load(item.highlightCover).saveDiskCacheStrategyAll()
		}

		binding.textView.text = item.highlightName.orEmpty()
	}
	val showHighlights = MutableLiveData(true)

	val adapterSocialMedia = RVItemCommonListUsage<ItemStoreDetailsSocialMediaBinding, ResponseStoreSocialMedia>(
		R.layout.item_store_details_social_media,
		onItemClick = { _, binding ->
			val item = binding.root.getTagJson<ResponseStoreSocialMedia>() ?: return@RVItemCommonListUsage

			val context = binding.root.context ?: return@RVItemCommonListUsage

			if (item.storeDetailsType == ResponseStoreSocialMedia.StoreDetailsType.COPY_LINK) {
				context.copyToClipboard(item.linkUrl.orEmpty())
			}else {
				context.launchBrowser(item.linkUrl.orEmpty())
			}
		}
	) { binding, _, item ->
		binding.root.setTagJson(item)

		binding.imageView.setImageResource(item.storeDetailsType?.drawableRes.orZero())
		binding.textView.text = item.storeDetailsType?.stringRes?.let { app.getString(it) }.orEmpty()
	}
	val showSocialMedia = MutableLiveData(true)

	val description = response.map {
		it?.description.orEmpty()
	}

	val showAdsNotExplore = MutableLiveData(true)

	val adapterExplores = RVItemCommonListUsageWithExoPlayer<ItemHomeExploreBinding, ItemHomeExplore>(
		R.layout.item_home_explore,
		onItemClick = { adapter, binding ->
			/*val item = (binding.constraintLayout.tag as? String).fromJsonInlinedOrNull<ItemHomeExplore>()
				?: return@RVItemCommonListUsageWithExoPlayer*/

			val position = binding.indicatorImageView.tag as? Int
				?: return@RVItemCommonListUsageWithExoPlayer

			val list = adapter.list.toMutableList().also {
				it.swap(0, position)
			}.toList()

			binding.root.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.home.explore.subsection",
				paths = arrayOf(list.toJsonInlinedOrNull(), (-1).toString())
			)
		}
	) { binding, position, item ->
		//val context = binding.root.context ?: return@RVItemCommonListUsageWithExoPlayer

		binding.constraintLayout.tag = item.toJsonInlinedOrNull()
		binding.indicatorImageView.tag = position

		val isVideo = item.isVideo

		this.releasePlayer()
		binding.playerView.player = null
		binding.playerView.isVisible = false
		//binding.imageImageView.isVisible = isVideo.not()
		binding.imageImageView.setImageResource(0)

		binding.indicatorImageView.setImageResource(
			when {
				isVideo -> R.drawable.video_indicator
				item.files.orEmpty().size > 1 -> R.drawable.multi_image_indicator
				else -> 0
			}
		)

		binding.likeTextView.text = item.likesCount.orZero().toString()

		binding.chatTextView.text = item.commentsCount.orZero().toString()

		if (isVideo) {
			val videoLink = item.files?.firstOrNull()
			if (videoLink != null && videoLink.isNotEmpty()) {
				binding.imageImageView.setupWithGlide {
					load(item.files?.firstOrNull()).asVideo().saveDiskCacheStrategyAll()
				}
			}
		}else {
			binding.imageImageView.setupWithGlide {
				load(item.files?.firstOrNull()).saveDiskCacheStrategyAll()
			}
		}
	}

	val allCategories = MutableLiveData<List<ItemCategory>?>(null)
	val allSubCategories = MutableLiveData<List<ItemSubCategory>?>(null)
	val selectedCategory = MutableLiveData<ItemCategory?>()
	val toBeShownSubCategories = selectedCategory.map {
		selectedCategory.value?.subCategories ?: allSubCategories.value.orEmpty()
	}
	val selectedSubCategory = MutableLiveData<ItemSubCategory?>()
	val filter = MutableLiveData(FilterAllFragment.Filter())
	val searchQuery = MutableLiveData("")
	val toBeShownAds = switchMapMultiple2(response, selectedCategory, selectedSubCategory, filter, searchQuery) {
		var ads = response.value?.advertisements.orEmpty()
		if (ads.isEmpty()) return@switchMapMultiple2 ads

		selectedSubCategory.value?.also { selectedSubCategory ->
			ads = ads.filter { it.storeSubCategoryId == selectedSubCategory.id }
		} ?: selectedCategory.value?.also { selectedCategory ->
			ads = ads.filter { it.storeCategoryId == selectedCategory.id }
		}

		searchQuery.value.ifNotNullNorEmpty { searchQuery ->
			ads = ads.filter { searchQuery in it.title.orEmpty() }
		}

		filter.value?.also { filter ->
			// todo

			/*
			val search: String? = null,
		val categoryId: Int? = null,
		val subCategoryId: Int? = null,
		val brandId: Int? = null,
		val minPrice: Float? = null,
		val maxPrice: Float? = null,
		val cityId: Int? = null,
		val areasIds: List<Int>? = null,
		val properties: List<DynamicFilterProperty> = emptyList(),
		val sortBy: SortBy? = null,
		val adType: AdType? = null,
		val rating: Int? = null,
			 */
		}

		ads
	}

	val layoutIsTwoColNotOneCol = MutableLiveData(false)

	@Suppress("RemoveExplicitTypeArguments")
	val adapterAds = RVItemCommonListUsageWithDifferentItems<ItemAdvertisementInResponseHome>(
		getLayoutRes = {
			if (layoutIsTwoColNotOneCol.value == true) {
				R.layout.item_home_rv_adv
			}else {
				R.layout.item_search_result
			}
		},
		onItemClick = { _, binding ->
			val context = binding.root.context ?: return@RVItemCommonListUsageWithDifferentItems

			val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
				?: return@RVItemCommonListUsageWithDifferentItems

			userLocalUseCase.goToAdvDetailsCheckIfMyAdv(
				context,
				binding.root.findNavController(),
				item
			)
		},
		additionalListenersSetups = { adapter, binding ->
			when (binding) {
				is ItemHomeRvAdvBinding -> {
					val listener = View.OnClickListener {
						val context = binding.root.context ?: return@OnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@OnClickListener

						//val position = binding.linearLayout.tag as? Int ?: return@OnClickListener

						userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
							context,
							binding.root.findNavController(),
							item.store
						)
					}
					binding.storeImageImageView.setOnClickListener(listener)
					binding.storeTextView.setOnClickListener(listener)
					binding.favImageView.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							context.applicationScope?.launch {
								repoShop.favOrUnFavAdv(item.id.orZero())
							}

							adapter.updateItem(
								position,
								item.copy(isFavorite = item.isFavorite.orFalse().not())
							)
						}
					}

					binding.whatsAppImageView.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						context.launchWhatsApp(item.phone.orEmpty())
					}
					binding.callImageView.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						context.launchDialNumber(item.phone.orEmpty())
					}
					binding.chatImageView.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							item.store?.also {
								context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
							}
						}
					}
				}
				is ItemSearchResultBinding -> {
					val listener = View.OnClickListener {
						val context = binding.root.context ?: return@OnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@OnClickListener

						//val position = binding.linearLayout.tag as? Int ?: return@OnClickListener

						userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
							context,
							binding.root.findNavController(),
							item.store
						)
					}
					binding.storeImageImageView.setOnClickListener(listener)
					binding.storeTextView.setOnClickListener(listener)
					binding.favImageView.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							context.applicationScope?.launch {
								repoShop.favOrUnFavAdv(item.id.orZero())
							}

							adapter.updateItem(
								position,
								item.copy(isFavorite = item.isFavorite.orFalse().not())
							)
						}
					}

					binding.whatsAppConstraintLayout.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						context.launchWhatsApp(item.phone.orEmpty())
					}
					binding.phoneConstraintLayout.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						context.launchDialNumber(item.phone.orEmpty())
					}
					binding.chatConstraintLayout.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							item.store?.also {
								context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
							}
						}
					}
				}
			}
		}
	) { binding, position, item ->
		binding.root.tag = item.toJsonInlinedOrNull()
		binding.root.setTag(R.id.position_tag, position)

		//val context = binding.root.context ?: return@RVPagingItemCommonListUsageWithDifferentItems

		when (binding) {
			is ItemHomeRvAdvBinding -> {
				binding.imageImageView.setupWithGlide {
					load(item.image).saveDiskCacheStrategyAll()
				}

				binding.premiumImageView.isVisible = item.isPremium

				binding.favImageView.setImageResource(
					if (item.isFavorite.orFalse()) R.drawable.item_adv_fav_med_cropped else R.drawable.item_adv_fav_cropped
				)

				binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
					.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

				binding.favsTextView.text = item.favoriteCount.orZero().toStringOrEmpty()

				binding.viewsTextView.text = item.viewsCount.orZero().toStringOrEmpty()

				binding.nameTextView.text = item.title

				binding.timeTextView.text = item.createdAt.orEmpty()

				binding.placeTextView.text = "${item.country?.name.orEmpty()} / ${item.city?.name.orEmpty()}"

				binding.storeImageImageView.setupWithGlide {
					load(item.store?.image).saveDiskCacheStrategyAll()
				}

				binding.storeTextView.text = item.store?.name

				binding.priceTextView.text = "${item.price?.round(2).orZero()} ${item.country?.currency.orEmpty()}"

				binding.negotiableTextView.isVisible = item.isNegotiable
			}
			is ItemSearchResultBinding -> {
				binding.imageImageView.setupWithGlide {
					load(item.image)
				}

				binding.premiumImageView.isVisible = item.isPremium

				binding.favImageView.setImageResource(
					if (item.isFavorite.orFalse()) R.drawable.item_adv_fav_med_cropped else R.drawable.item_adv_fav_large_cropped
				)

				binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
					.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

				binding.favsTextView.text = item.favoriteCount.orZero().toStringOrEmpty()

				binding.viewsTextView.text = item.viewsCount.orZero().toStringOrEmpty()

				binding.nameTextView.text = item.title

				binding.timeTextView.text = item.createdAt.orEmpty()

				binding.placeTextView.text = "${item.country?.name.orEmpty()} / ${item.city?.name.orEmpty()}"

				binding.storeImageImageView.setupWithGlide {
					load(item.store?.image)
				}

				binding.storeTextView.text = item.store?.name

				binding.priceTextView.text = item.price?.round(2).orZero().toString()
				binding.currencyTextView.text = item.country?.currency.orEmpty()

				binding.negotiableTextView.isVisible = item.isNegotiable
			}
		}
	}

	val adapterCategories = RVItemCommonListUsage<ItemStoreCategoryInMyAdsBinding, ItemCategory>(
		R.layout.item_store_category_in_my_ads,
		onItemClick = { adapter, binding ->
			val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemCategory>() ?: return@RVItemCommonListUsage
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

	val adapterSubCategories = RVItemCommonListUsage<ItemStoreCategoryInMyAdsBinding, ItemSubCategory>(
		R.layout.item_store_category_in_my_ads,
		onItemClick = { adapter, binding ->
			val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemSubCategory>() ?: return@RVItemCommonListUsage
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

	fun toggleLayout() {
		layoutIsTwoColNotOneCol.toggleValue()
	}

	fun filter(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<String>(
			FilterAllFragment::class.java.name
		) { specialFilterString ->
			val filter = FilterAllFragment.Filter.fromSpecialString(specialFilterString)

			this.filter.value = this.filter.value?.copy(
				search = filter.search,
				categoryId = filter.categoryId,
				subCategoryId = filter.subCategoryId,
				brandId = filter.brandId,
				minPrice = filter.minPrice,
				maxPrice = filter.maxPrice,
				cityId = filter.cityId,
				areasIds = filter.areasIds,
				properties = filter.properties,
				sortBy = filter.sortBy,
				adType = filter.adType,
				rating = filter.rating,
			)

			searchQuery.postValue(
				filter.search.orEmpty()
			)
		}

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.filter.all",
			paths = arrayOf(
				true.toString(),
				filter.value?.toSpecialString().orStringNullIfNullOrEmpty(),
				true.toString()
			)
		)
	}

	fun sort(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<FilterAllFragment.SortBy>(
			AdsSortDialogFragment::class.java.name
		) {
			filter.value = filter.value?.copy(
				sortBy = it
			)
		}

		AdsSortDialogFragment.launch(
			view.findNavController(),
			"Empty",
			filter.value?.sortBy
		)
	}

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

	fun shareThisScreen(view: View) {
		val link = response.value?.shareLink.orEmpty()

		if (link.isEmpty()) return view.context.showError(view.context.getString(R.string.no_link_exists))

		view.context?.applicationScope?.launch {
			repoShop.shareStore(response.value?.id.orZero())
		}

		view.context.launchShareText(link)
	}

	fun showLocation(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val latitude = response.value?.latitude?.toDoubleOrNull()?.toString()
		val longitude = response.value?.longitude?.toDoubleOrNull()?.toString()

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
				isFollowing = response.value?.isFollowing?.not()
			)

			context.applicationScope?.launch {
				repoShop.followStore(response.value?.id.orZero())
			}
		}
	}

	fun goToViews(view: View) {
		SimpleUserListOfInteractionsFragment.launch(
			view.findNavController(),
			app.getString(R.string.views),
			"${app.getString(R.string.views_count)} ${viewsCount.value.orEmpty()}",
			SimpleUserListOfInteractionsFragment.Type.STORE_VIEWS,
			response.value?.id.orZero()
		)
	}

	fun goToReviews(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.adv.clients.reviews.with.option",
			paths = arrayOf(
				response.value?.id.orZero().toString(),
				true.toString(), // useRating
				false.toString(),
			)
		)
	}

	fun goToFollowers(view: View) {
		SimpleUserListOfInteractionsFragment.launch(
			view.findNavController(),
			app.getString(R.string.followers),
			"${app.getString(R.string.followers_count)} ${followersCount.value.orEmpty()}",
			SimpleUserListOfInteractionsFragment.Type.STORE_FOLLOWERS,
			response.value?.id.orZero()
		)
	}

	fun goToAdvertisements(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		fragment.binding.nestedScrollView.smoothScrollBy(0, fragment.binding.labelAdsTextView.top)

		if (showAdsNotExplore.value != true) {
			showAdsNotExplore.value = true
		}
	}
	
	fun showWorkingHours(view: View) {
		WorkingHoursDialogFragment.launch(
			view.findNavController(),
			response.value?.workingHours.orEmpty()
		)
	}

	fun reportStore(view: View) {
		ReportingDialogFragment.launch(
			view.findNavController(),
			ReportingDialogFragment.Type.REPORT_STORES,
			response.value?.id.orZero()
		)
	}

	fun blockStore(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<Int>(ReportingDialogFragment.Type.BLOCK_STORES.name) {
			if (it == response.value?.id) {
				// todo tell previous screens as well which is kinda of a plumber isa.
				// Easily check all not on view created fetch data ex. home screen ads & stores isa.
				fragment.findNavController().navigateUp()
			}
		}

		ReportingDialogFragment.launch(
			view.findNavController(),
			ReportingDialogFragment.Type.BLOCK_STORES,
			response.value?.id.orZero()
		)
	}

	fun showAds() {
		showAdsNotExplore.value = true
	}
	fun showExplore() {
		showAdsNotExplore.value = false
	}

	fun goToWhatsApp(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val context = fragment.context ?: return

		context.launchWhatsApp(response.value?.fullPhone.orEmpty())
	}

	fun makeCall(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val context = fragment.context ?: return

		context.launchDialNumber(response.value?.fullPhone.orEmpty())
	}

	fun chat(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val context = fragment.context ?: return

		if (context.isLoginWithOpenAuth()) {
			response.value?.also {
				context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
			}
		}
	}

}
