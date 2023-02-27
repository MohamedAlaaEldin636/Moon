package grand.app.moon.presentation.home

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.launchCometChat
import grand.app.moon.databinding.*
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.home.models.StoreModel
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.serDrawableCompatBA
import grand.app.moon.extensions.bindingAdapter.setCompoundDrawablesRelativeWithIntrinsicBoundsStart
import grand.app.moon.extensions.bindingAdapter.visibleOrInvisible
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.models.*
import grand.app.moon.presentation.home.viewModels.Home2ViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun Home2ViewModel.getAdapterStories(): RVItemCommonListUsageWithDifferentItems<ResponseStory> = RVItemCommonListUsageWithDifferentItems(
	getLayoutRes = {
		if (it == 0) R.layout.item_home_rv_story_addition else R.layout.item_home_rv_story_actual
	},
	onItemClick = { adapter, binding ->
		val fragment = binding.root.findFragmentOrNull<Home2Fragment>()
			?: return@RVItemCommonListUsageWithDifferentItems
		val context = fragment.context ?: return@RVItemCommonListUsageWithDifferentItems

		when (binding) {
			is ItemHomeRvStoryAdditionBinding -> {
				if (fragment.context?.isLoginWithOpenAuth().orFalse()) {
					if (fragment.viewModel.userLocalUseCase().isStore.orFalse()) {
						fragment.handleRetryAbleActionCancellable(
							action = {
								repoHome2.checkAvailabilityForStories()
							}
						) { count ->
							if (count > 0) {
								fragment.findNavController().navigateDeepLinkWithOptions(
									"fragment-dest",
									"grand.app.moon.dest.add.story"
								)
							}else {
								fragment.showError(fragment.getString(R.string.no_more_rem_stories_in_your_package))
							}
						}
					}else {
						fragment.findNavController().navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest.become.shop.packages"
						)
					}
				}
			}
			is ItemHomeRvStoryActualBinding -> {
				val newList = adapter.list.drop(1)

				val position = binding.root.tag as? Int ?: return@RVItemCommonListUsageWithDifferentItems

				val toBeUsedListJson = newList.toJsonInlinedOrNull().orEmpty()
				val toBeUsedPosition = position - 1

				val storyModel = StoreModel()
				storyModel.list.addAll(newList.map { it.toStore() })
				storyModel.position = position - 1

				val item = adapter.list[position]
				if (context.isLogin() && item.isSeen.not()) {
					item.stories?.firstOrNull()?.isSeen = true

					if (item.isSouqMoonStory.orFalse().not() && item.isSeen) {
						val updatedList = adapter.list.toMutableList()
						val updatedItem = updatedList.removeAt(position)
						updatedList += updatedItem

						adapter.submitList(updatedList)
					}
				}

				fragment.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.story.player",
					paths = arrayOf(toBeUsedListJson, toBeUsedPosition.toString())
				)
			}
		}
	}
) { binding, position, item ->
	binding.root.tag = position

	when (binding) {
		is ItemHomeRvStoryActualBinding -> {
			binding.storeNameTextView.text = item.name

			binding.seenCircleView.visibleOrInvisible(
				item.isSeen.not()
			)

			binding.storeLogoImageView.setupWithGlide {
				load(item.image)
					.error(R.drawable.ic_logo_shop_in_create_shop)
			}

			val story = item.stories?.firstOrNull()

			binding.storyImageView.setupWithGlide {
				load(story?.file)
					.asVideoIfRequired(story?.isVideo.orFalse())
					.error(R.drawable.splash)
			}
		}
	}
}

@Suppress("unused")
fun Home2ViewModel.getAdapterCategories() = RVItemCommonListUsage<ItemHomeRvCategoryBinding, ItemCategory>(
	R.layout.item_home_rv_category,
	onItemClick = { _, binding ->
		val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemCategory>()
			?: return@RVItemCommonListUsage

		binding.root.findNavController().navigate(
			R.id.categoryDetailsFragment,
			bundleOf(
				"category_id" to item.id.orZero(),
				"tabBarText" to item.name.orEmpty()
			), Constants.NAVIGATION_OPTIONS
		)
	}
) { binding, _, item ->
	binding.root.tag = item.toJsonInlinedOrNull()

	binding.textTextView.text = item.name

	binding.imageImageView.setupWithGlide {
		load(item.image)
	}

}

fun Home2ViewModel.getAdapterForStores() = RVItemCommonListUsage<ItemHomeRvStoreBinding, ItemStoreInResponseHome>(
	R.layout.item_home_rv_store,
	onItemClick = { _, binding ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		val item = binding.root.getTagJson<ItemStoreInResponseHome>() ?: return@RVItemCommonListUsage

		userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
			context,
			binding.root.findNavController(),
			item
		)
	},
	additionalListenersSetups = { adapter, binding ->
		binding.followingButtonView.setOnClickListener { view ->
			val context = view.context ?: return@setOnClickListener

			val item = binding.root.getTagJson<ItemStoreInResponseHome>() ?: return@setOnClickListener
			val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

			if (context.isLoginWithOpenAuth()) {
				context.applicationScope?.launch {
					repoShop.followStore(item.id.orZero())
				}

				adapter.updateItem(
					position,
					item.copy(isFollowing = item.isFollowing.orFalse().not())
				)
			}
		}
	}
) { binding, position, item ->
	binding.root.tag = item.toJsonInlinedOrNull()
	binding.root.setTag(R.id.position_tag, position)

	val context = binding.root.context ?: return@RVItemCommonListUsage

	binding.imageImageView.setupWithGlide {
		load(item.image)
	}

	binding.nameTextView.text = item.name
	binding.nameTextView.setCompoundDrawablesRelativeWithIntrinsicBoundsStart(
		if (item.hasOffer.orFalse()) R.drawable.store_has_offer else 0
	)

	binding.nicknameTextView.text = item.nickname

	binding.ratingBar.setProgressBA((item.averageRate.orZero() * 20).roundToInt())

	binding.averageRateTextView.text = "( ${item.averageRate?.round(1).orZero()} )"

	binding.viewsTextView.text = item.viewsCount.toStringOrEmpty()

	binding.adsTextView.text = "${item.advertisementsCount.orZero()} ${context.getString(R.string.advertisement)}"

	binding.followingButtonTextView.text = if (item.isFollowing.orFalse()) {
		binding.followingButtonTextView.serDrawableCompatBA()

		context.getString(R.string.un_follow)
	}else {
		binding.followingButtonTextView.serDrawableCompatBA(
			start = ContextCompat.getDrawable(context, R.drawable.follow_add)
		)

		context.getString(R.string.follow)
	}

	binding.premiumImageView.isVisible = item.isPremium
}

fun Home2ViewModel.getAdapterForAds() = RVItemCommonListUsage<ItemHomeRvAdvBinding, ItemAdvertisementInResponseHome>(
	R.layout.item_home_rv_adv,
	onItemClick = { _, binding ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
			?: return@RVItemCommonListUsage

		userLocalUseCase.goToAdvDetailsCheckIfMyAdv(
			context,
			binding.root.findNavController(),
			item
		)
	},
	additionalListenersSetups = { adapter, binding ->
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
) { binding, position, item ->
	binding.root.setTagJson(item)
	binding.root.setTag(R.id.position_tag, position)

	binding.imageImageView.setupWithGlide {
		load(item.image)
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
		load(item.store?.image)
	}

	binding.storeTextView.text = item.store?.name

	binding.priceTextView.text = "${item.price?.round(2).orZero()} ${item.country?.currency.orEmpty()}"

	binding.negotiableTextView.isVisible = item.isNegotiable
}
