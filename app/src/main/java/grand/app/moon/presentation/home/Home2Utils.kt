package grand.app.moon.presentation.home

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import grand.app.moon.R
import grand.app.moon.databinding.*
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.adjustInsideRV
import grand.app.moon.extensions.bindingAdapter.serDrawableCompatBA
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.viewModels.Home2ViewModel
import kotlin.math.roundToInt

fun Home2ViewModel.getAdapterStories() = RVItemCommonListUsageWithDifferentItems<ResponseStory>(
	getLayoutRes = {
		if (it == 0) R.layout.item_home_rv_story_addition else R.layout.item_home_rv_story_actual
	},
	onItemClick = { adapter, binding ->
		val fragment = binding.root.findFragmentOrNull<Home2Fragment>()
			?: return@RVItemCommonListUsageWithDifferentItems

		when (binding) {
			is ItemHomeRvStoryAdditionBinding -> {
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
			}
			is ItemHomeRvStoryActualBinding -> {
				General.TODO("Not programmed yet")
			}
		}
	}
) { binding, position, item ->
	when (binding) {
		is ItemHomeRvStoryActualBinding -> {
			binding.storeNameTextView.text = item.name

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

fun Home2ViewModel.getAdapterCategories() = RVItemCommonListUsage<ItemHomeRvCategoryBinding, ItemCategory>(
	R.layout.item_home_rv_category,
	onItemClick = { adapter, binding ->
		General.TODO("not programmed yet")
	}
) { binding, position, item ->

	binding.textTextView.text = item.name

	binding.imageImageView.setupWithGlide {
		load(item.image)
	}

}

fun Home2ViewModel.getAdapterForStores() = RVItemCommonListUsage<ItemHomeRvStoreBinding, ItemStoreInResponseHome>(
	R.layout.item_home_rv_store,
	onItemClick = { adapter, binding ->
		General.TODO("ch 1")
	},
	additionalListenersSetups = { adapter, binding ->
		binding.followingButtonView.setOnClickListener {
			General.TODO("ch 2")
		}
	}
) { binding, position, item ->
	val context = binding.root.context ?: return@RVItemCommonListUsage

	binding.imageImageView.setupWithGlide {
		load(item.image)
	}

	binding.nameTextView.text = item.name

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
	onItemClick = { adapter, binding ->
		General.TODO("ch 1")
	},
	additionalListenersSetups = { adapter, binding ->
		binding.storeImageImageView.setOnClickListener {
			General.TODO("ch 2")
		}

		binding.whatsAppImageView.setOnClickListener {
			General.TODO("ch 2")
		}
		binding.callImageView.setOnClickListener {
			General.TODO("ch 2")
		}
		binding.chatImageView.setOnClickListener {
			General.TODO("ch 2")
		}
	}
) { binding, position, item ->
	binding.imageImageView.setupWithGlide {
		load(item.image)
	}

	binding.premiumImageView.isVisible = item.isPremium

	binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
		.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

	binding.favsTextView.text = item.favoriteCount.orZero().toStringOrEmpty()

	binding.viewsTextView.text = item.viewsCount.orZero().toStringOrEmpty()

	binding.nameTextView.text = item.title

	binding.timeTextView.adjustInsideRV(
		item.createdAt.orEmpty(),
		10f,
		3f
	)

	binding.placeTextView.adjustInsideRV(
		"${item.country?.name.orEmpty()} / ${item.city?.name.orEmpty()}",
		10f,
		3f
	)

	binding.storeImageImageView.setupWithGlide {
		load(item.store?.image)
	}

	binding.storeTextView.text = item.store?.name

	binding.priceTextView.text = "${item.price?.round(2).orZero()} ${item.country?.currency.orEmpty()}"

	binding.negotiableTextView.isVisible = item.isNegotiable
}