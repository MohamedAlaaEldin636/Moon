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
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrDefaultUserBA
import grand.app.moon.extensions.bindingAdapter.visibleOrInvisible
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.extensions.showError
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

	val isStoreAccount = response.map { it?.isStore }

	val isMyStore = response.map {
		app.isLogin() && userLocalUseCase().id == it?.id
	}

	val isSeen = response.map {
		it?.toResponseStory()?.isSeen.orFalse().not()
	}

	fun showStories(view: View) {
		if (response.value?.stories.isNullOrEmpty()) {
			return
		}

		val responseStory = response.value?.toResponseStory() ?: return

		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		if (responseStory.isSeen && responseStory.stories.orEmpty().size < 2) {
			response.value = response.value?.copy(
				stories = response.value?.stories?.copyUpdateAllItems { it.copy(isSeen = false) }
			)
		}

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.story.player",
			paths = arrayOf(listOf(responseStory).toJsonInlinedOrNull().orEmpty(), 0.toString())
		)
	}

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
	val showGeneralAddress = response.map {
		it?.country?.name.isNullOrEmpty().not() && it?.city?.name.isNullOrEmpty().not()
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

	val adapterHighlights = RVItemCommonListUsage<ItemStoryInStoreDetailsBinding, ResponseStory.Story>(
		R.layout.item_story_in_store_details,
		onItemClick = { adapter, binding ->
			val fragment = binding.root.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return@RVItemCommonListUsage
			val context = fragment.context ?: return@RVItemCommonListUsage

			val position = binding.root.tag as? Int ?: return@RVItemCommonListUsage

			val item = adapter.list[position]
			if (context.isLogin() && item.isSeen.orFalse().not()) {
				item.isSeen = true

				if (item.isSeen == true) {
					val updatedList = adapter.list.toMutableList()
					val updatedItem = updatedList.removeAt(position)
					updatedList += updatedItem

					adapter.submitList(updatedList)
				}
			}

			val responseStory = response.value?.toResponseStory()?.copy(
				stories = listOf(item)
			) ?: return@RVItemCommonListUsage

			fragment.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.story.player",
				paths = arrayOf(listOf(responseStory).toJsonInlinedOrNull().orEmpty(), 0.toString())
			)
		}
	) { binding, position, item ->
		binding.root.tag = position

		binding.view.visibleOrInvisible(item.isSeen.orFalse().not())

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
			}.onEach {
				it.store = response.value?.toItemHomeExplore()
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
	private val selectedCategory = MutableLiveData<ItemCategory?>()
	val toBeShownSubCategories = switchMapMultiple2(allCategories, allSubCategories, selectedCategory) {
		val allSubCategories = allSubCategories.value.orEmpty()
		listOf(ItemSubCategory(null, getString(R.string.all))).plus(
			selectedCategory.value?.let { item ->
				allCategories.value.orEmpty().firstOrNull { item.id == it.id }?.subCategories.orEmpty()
			} ?: allSubCategories
		)
	}
	private val selectedSubCategory = MutableLiveData<ItemSubCategory?>()
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
			filter.categoryId.ifNotNull { categoryId ->
				ads = ads.filter { it.category?.id == categoryId }
			}

			filter.subCategoryId.ifNotNull { subCategoryId ->
				ads = ads.filter { it.subCategoryId == subCategoryId }
			}

			filter.brandId.ifNotNull { brandId ->
				ads = ads.filter { it.brandId == brandId }
			}

			filter.minPrice.ifNotNull { minPrice ->
				ads = ads.filter { it.price != null && it.price.orZero() >= minPrice }
			}

			filter.maxPrice.ifNotNull { maxPrice ->
				ads = ads.filter { it.price != null && it.price.orZero() <= maxPrice }
			}

			filter.cityId.ifNotNull { cityId ->
				ads = ads.filter { it.city?.id == cityId }
			}

			filter.areasIds.ifNotNull { areasIds ->
				ads = ads.filter { it.area?.id in areasIds }
			}

			filter.properties // todo

			filter.sortBy.ifNotNull { sortBy ->
				ads = when (sortBy) {
					FilterAllFragment.SortBy.NEWEST -> ads.sortedByDescending { item ->
						item.dateInMs
					}
					FilterAllFragment.SortBy.OLDEST -> ads.sortedBy { item ->
						item.dateInMs
					}
					FilterAllFragment.SortBy.LEAST_PRICE -> ads.sortedBy { item ->
						item.price
					}
					FilterAllFragment.SortBy.HIGHEST_PRICE -> ads.sortedByDescending { item ->
						item.price
					}
				}
			}

			filter.adType.ifNotNull { adType ->
				when (adType) {
					FilterAllFragment.AdType.ALL -> { /* Do nothing */ }
					FilterAllFragment.AdType.PREMIUM -> ads = ads.filter { it.isPremium }
					FilterAllFragment.AdType.FREE -> ads = ads.filter { it.isPremium.not() }
				}
			}

			filter.rating.ifNotNull { rating ->
				ads = ads.filter { it.averageRate.orZero() >= rating.toFloat() }
			}
		}

		ads
	}

	val layoutIsTwoColNotOneCol = MutableLiveData(false)

	// to-do .adjustIn kaza isa.
	/*
	binding.fromTextView.adjustInsideRV(
			item.from.toAppTimeFormat(context),
			10.5f,
			6f
		)

		viewModel.showPremium ? @string/check_ad_premium : @string/prem_ad

		gher on clicks
	 */
	@Suppress("RemoveExplicitTypeArguments")
	val adapterAds = RVItemCommonListUsageWithDifferentItems<ItemAdvertisementInResponseHome>(
		getLayoutRes = {
			if (layoutIsTwoColNotOneCol.value == true) {
				if (isMyStore.value == true) R.layout.item_home_rv_adv_my_adv else R.layout.item_home_rv_adv
			}else {
				if (isMyStore.value == true) R.layout.item_search_result_my_adv else R.layout.item_search_result
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

						context.applicationScope?.launch {
							repoShop.interactionForAdWhatsApp(item.id.orZero())
						}

						context.launchWhatsApp(item.store?.fullWhatsAppPhone.orEmpty())
					}
					binding.callImageView.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						context.applicationScope?.launch {
							repoShop.interactionForAdCall(item.id.orZero())
						}

						context.launchDialNumber(item.store?.fullAdsPhone.orEmpty())
					}
					binding.chatImageView.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							context.applicationScope?.launch {
								repoShop.interactionForAdChat(item.id.orZero())
							}

							item.store?.also {
								context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
							}
						}
					}
				}
				is ItemHomeRvAdvMyAdvBinding -> {
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

					binding.editImageView.setOnClickListener {
						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						binding.root.findFragmentOrNull<OtherStoreDetailsFragment>()?.editMyAdv(item)
					}
					binding.promotionImageView.setOnClickListener {
						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						binding.root.findFragmentOrNull<OtherStoreDetailsFragment>()?.makeMyAdvPremiumOrCheckAdvPremium(item)
					}
					binding.delImageView.setOnClickListener {
						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						binding.root.findFragmentOrNull<OtherStoreDetailsFragment>()?.delMyAdv(item)
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

						context.applicationScope?.launch {
							repoShop.interactionForAdWhatsApp(item.id.orZero())
						}

						context.launchWhatsApp(item.store?.fullWhatsAppPhone.orEmpty())
					}
					binding.phoneConstraintLayout.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						context.applicationScope?.launch {
							repoShop.interactionForAdCall(item.id.orZero())
						}

						context.launchDialNumber(item.store?.fullAdsPhone.orEmpty())
					}
					binding.chatConstraintLayout.setOnClickListener {
						val context = binding.root.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							context.applicationScope?.launch {
								repoShop.interactionForAdChat(item.id.orZero())
							}

							item.store?.also {
								context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
							}
						}
					}
				}
				is ItemSearchResultMyAdvBinding -> {
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

					binding.editAdvTextView.setOnClickListener {
						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						binding.root.findFragmentOrNull<OtherStoreDetailsFragment>()?.editMyAdv(item)
					}
					binding.promotionAdvTextView.setOnClickListener {
						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						binding.root.findFragmentOrNull<OtherStoreDetailsFragment>()?.makeMyAdvPremiumOrCheckAdvPremium(item)
					}
					binding.delAdvTextView.setOnClickListener {
						val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
							?: return@setOnClickListener

						binding.root.findFragmentOrNull<OtherStoreDetailsFragment>()?.delMyAdv(item)
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

				binding.storeImageImageView.setupWithGlideOrDefaultUserBA(item.store?.image)

				binding.storeTextView.text = item.store?.name

				binding.priceTextView.text = "${item.price?.round(2).orZero()} ${item.country?.currency.orEmpty()}"

				binding.negotiableTextView.isVisible = item.isNegotiable
			}
			is ItemHomeRvAdvMyAdvBinding -> {
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

				binding.storeImageImageView.setupWithGlideOrDefaultUserBA(item.store?.image)

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

				binding.storeImageImageView.setupWithGlideOrDefaultUserBA(item.store?.image)

				binding.storeTextView.text = item.store?.name

				binding.priceTextView.text = item.price?.round(2).orZero().toString()
				binding.currencyTextView.text = item.country?.currency.orEmpty()

				binding.negotiableTextView.isVisible = item.isNegotiable
			}
			is ItemSearchResultMyAdvBinding -> {
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

				binding.storeImageImageView.setupWithGlideOrDefaultUserBA(item.store?.image)

				binding.storeTextView.text = item.store?.name

				binding.priceTextView.text = item.price?.round(2).orZero().toString()
				binding.currencyTextView.text = item.country?.currency.orEmpty()

				binding.negotiableTextView.isVisible = item.isNegotiable

				binding.promotionAdvTextView.text = app.getString(
					if (item.isPremium) R.string.check_ad_premium else R.string.prem_ad
				)
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

				adapterSubCategories.notifyDataSetChanged()

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

			val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

			fragment.setFragmentResultUsingJson(
				OtherStoreDetailsFragment.KEY_RESULT_IS_FOLLOWING,
				response.value?.isFollowing.orFalse()
			)

			context.applicationScope?.launch {
				repoShop.followStore(response.value?.id.orZero())

				context.followOrUnFollowStoreFromNotHomeScreen(response.value?.toItemStoreInResponseHome())
			}
		}
	}

	fun goToViews(view: View) {
		val context = view.context ?: return
		if (context.isLogin().not() || (userLocalUseCase().isStore.orFalse().not() && userLocalUseCase().id != response.value?.id)) return

		SimpleUserListOfInteractionsFragment.launch(
			view.findNavController(),
			app.getString(R.string.views),
			"${app.getString(R.string.views_count)} ${viewsCount.value.orEmpty()}",
			SimpleUserListOfInteractionsFragment.Type.STORE_VIEWS,
			response.value?.id.orZero()
		)
	}

	fun goToReviews(view: View) {
		val context = view.context ?: return
		if (context.isLogin().not() || (userLocalUseCase().isStore.orFalse().not() && userLocalUseCase().id != response.value?.id)) return

		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.adv.clients.reviews.with.option",
			paths = arrayOf(
				response.value?.id.orZero().toString(),
				(app.isLogin() && response.value?.id != userLocalUseCase().id).toString(), // useRating
				response.value?.id.toString(),
				false.toString(),
			)
		)
	}

	fun goToFollowers(view: View) {
		val context = view.context ?: return
		if (context.isLogin().not() || (userLocalUseCase().isStore.orFalse().not() && userLocalUseCase().id != response.value?.id)) return

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
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val context = fragment.context ?: return

		if (context.isLoginWithOpenAuth()) {
			ReportingDialogFragment.launch(
				view.findNavController(),
				ReportingDialogFragment.Type.REPORT_STORES,
				response.value?.id.orZero()
			)
		}
	}

	fun blockStore(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val context = fragment.context ?: return

		if (context.isLoginWithOpenAuth()) {
			fragment.setFragmentResultListenerUsingJson<Int>(ReportingDialogFragment.Type.BLOCK_STORES.name) {
				if (it == response.value?.id) {
					// Easily check all not on view created fetch data ex. home screen ads & stores isa.
					//fragment.findNavController().navigateUp()
					fragment.activity?.openActivityAndClearStack(HomeActivity::class.java)
				}
			}

			ReportingDialogFragment.launch(
				view.findNavController(),
				ReportingDialogFragment.Type.BLOCK_STORES,
				response.value?.id.orZero()
			)
		}
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

		context.applicationScope?.launch {
			repoShop.interactionForStoreWhatsApp(response.value?.id.orZero())
		}

		MyLogger.e("phoneeeeeeeeeee -> ${response.value?.fullAdsPhone.orEmpty()}")

		context.launchWhatsApp(response.value?.fullWhatsAppPhone.orEmpty())
	}

	fun makeCall(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val context = fragment.context ?: return

		context.applicationScope?.launch {
			repoShop.interactionForStoreCall(response.value?.id.orZero())
		}

		MyLogger.e("phoneeeeeeeeeee -> ${response.value?.fullAdsPhone.orEmpty()}")

		context.launchDialNumber(response.value?.fullAdsPhone.orEmpty())
	}

	fun chat(view: View) {
		val fragment = view.findFragmentOrNull<OtherStoreDetailsFragment>() ?: return

		val context = fragment.context ?: return

		if (context.isLoginWithOpenAuth()) {
			context.applicationScope?.launch {
				repoShop.interactionForStoreChat(response.value?.id.orZero())
			}

			response.value?.also {
				context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
			}
		}
	}

	private fun BaseFragment<*>.editMyAdv(item: ItemAdvertisementInResponseHome) {
		val list = repoShop.getCategoriesWithSubCategoriesAndBrands()

		val categoryId = item.category?.id.orZero()

		val category = list.firstOrNull { it.id == categoryId }
			?: return showError(getString(R.string.something_went_wrong_please_try_again))

		handleRetryAbleActionCancellable(
			action = {
				adsUseCase.getMyAdvertisementDetails(item.id.orZero())
			}
		) { response ->
			//fragment-dest://grand.app.moon.dest.add.adv.final.page/{idOfMainCategory}/{idOfSubCategory}/{jsonListOfBrands}/{jsonResponseMyAdvDetails}
			findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.add.adv.final.page",
				paths = arrayOf(
					category.id.orZero().toString(),
					item.subCategoryId.orZero().toString(),
					category.brands.toJsonInlinedOrNull().orStringNullIfNullOrEmpty(),
					response.toJsonInlinedOrNull().orStringNullIfNullOrEmpty()
				)
			)
			/*findNavController().navigate(
				MyAdvDetailsFragmentDirections.actionDestMyAdvDetailsToDestAddAdvFinalPage(
					category.id.orZero(),
					item.subCategoryId.orZero(),
					category.brands.toJsonInlinedOrNull().orStringNullIfNullOrEmpty(),
					response.toJsonInlinedOrNull().orStringNullIfNullOrEmpty()
				)
			)*/
		}
	}

	private fun BaseFragment<*>.makeMyAdvPremiumOrCheckAdvPremium(item: ItemAdvertisementInResponseHome) {
		if (item.isPremium) {
			//fragment-dest://grand.app.moon.dest.my.ad.or.shop.package/{advertisementId}/{isAdvNotShop}
			findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.my.ad.or.shop.package",
				paths = arrayOf(
					item.id.orZero().toString(),
					true.toString()
				)
			)
			/*findNavController().navigateSafely(
				MyAdvDetailsFragmentDirections.actionDestMyAdvDetailsToDestMyAdOrShopPackage(
					item.id.orZero(), true
				)
			)*/

			return
		}

		handleRetryAbleActionCancellable(
			action = {
				homeUseCase.getCheckAvailabilityForPremiumAds()
			}
		) { availableAds ->
			MyLogger.e("availableAds $availableAds")

			if (availableAds > 0) {
				updateMyAdvertisementToBePremium(item)
			}else {
				//fragment-dest://grand.app.moon.dest.make.adv.or.store.premium/{advertisementId}
				findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.make.adv.or.store.premium",
					paths = arrayOf(item.id.orZero().toString())
				)
				/*
				fragment.findNavController().setResultInPreviousBackStackEntrySavedStateHandleViaGson(
							MyAdsFragment.NewAdvertisementState.BECAME_PREMIUM
						)
				 */
				/*findNavController().navigateSafely(
					MyAdvDetailsFragmentDirections.actionDestMyAdvDetailsToDestMakeAdvOrStorePremium(
						item.id.orZero()
					)
				)*/
			}
		}
	}

	private fun BaseFragment<*>.updateMyAdvertisementToBePremium(item: ItemAdvertisementInResponseHome) {
		handleRetryAbleActionCancellableNullable(
			action = {
				adsUseCase.updateAdvertisementToBePremium(item.id.orZero())
			}
		) {
			afterMyAdvBecamePremium(item)
		}
	}

	@Suppress("unused")
	private fun BaseFragment<*>.afterMyAdvBecamePremium(item: ItemAdvertisementInResponseHome) {
		adapterAds.list.indexOfFirstOrNull { it.id == item.id }?.also { index ->
			val newItem = adapterAds.list.getOrNull(index)?.also { it.makePremium() }
			if (newItem != null) {
				adapterAds.updateItem(index, newItem)
			}
		}

		/*findNavController().setResultInPreviousBackStackEntrySavedStateHandleViaGson(
			MyAdsFragment.NewAdvertisementChange(
				item.id.orZero(),
				MyAdsFragment.NewAdvertisementState.BECAME_PREMIUM
			)
		)*/
	}

	private fun BaseFragment<*>.delMyAdv(item: ItemAdvertisementInResponseHome) {
		showCustomYesNoWarningDialog(
			getString(R.string.confirm_deletion),
			getString(R.string.are_you_sure_del_ad)
		) { dialog ->
			handleRetryAbleActionCancellableNullable(
				action = {
					adsUseCase.deleteAdvertisement(item.id.orZero())
				}
			) {
				dialog.dismiss()

				showMessage(getString(R.string.done_successfully))

				adapterAds.list.indexOfFirstOrNull { it.id == item.id }?.also { index ->
					val newItem = adapterAds.list.getOrNull(index)?.also { it.makePremium() }
					if (newItem != null) {
						adapterAds.deleteAt(index)
					}
				}

				/*findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
					MyAdsFragment.NewAdvertisementChange(
						item.id.orZero(),
						MyAdsFragment.NewAdvertisementState.DELETED
					)
				)*/
			}
		}
	}

}
