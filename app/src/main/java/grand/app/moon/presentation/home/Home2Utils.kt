package grand.app.moon.presentation.home

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import grand.app.moon.R
import grand.app.moon.databinding.*
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.home.models.StoreModel
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.adjustInsideRV
import grand.app.moon.extensions.bindingAdapter.serDrawableCompatBA
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.models.toStore
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
				val newList = adapter.list.drop(1)

				val position = binding.root.tag as? Int ?: return@RVItemCommonListUsageWithDifferentItems

				val storyModel = StoreModel()
				storyModel.list.addAll(newList.map { it.toStore() })
				storyModel.position = position - 1

				fragment.findNavController()
					.navigateSafely(Home2FragmentDirections.actionDestHome2ToStoryFragment(storyModel))
				/*

            R.id.home_fragment -> holder.itemLayoutBinding.shapeableImageView.findFragment<HomeFragment>()
              .navigateSafe(HomeFragmentDirections.actionHomeFragmentToStoryFragment(storyModel))
				 */
			}
		}
	}
) { binding, position, item ->
	binding.root.tag = position

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
		val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemAdvertisementInResponseHome>()
			?: return@RVItemCommonListUsage

		if (item.store?.id == userLocalUseCase().id) {
			kotlin.runCatching {
				binding.root.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
					paths = arrayOf(item.id.orZero().toString())
				)
			}
			// my ad
			// "fragment-dest:///{id}" />
		}else {

		}
		/*
		R.id.nav_ads, bundleOf(
				        "id" to data.id,
				        "type" to type
			        )
		 */
		//General.TODO("ch 1")
		/*binding.root.findNavController().navigate(
			R.id.nav_ads, bundleOf(
				"id" to data.id,
				"type" to type
			)
		)*/
	},
	additionalListenersSetups = { adapter, binding ->
		binding.storeImageImageView.setOnClickListener {
			General.TODO("ch 2")
		}
		binding.favImageView.setOnClickListener {
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
	binding.root.tag = item.toJsonInlinedOrNull()

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
