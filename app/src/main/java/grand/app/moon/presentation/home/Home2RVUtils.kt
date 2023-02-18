package grand.app.moon.presentation.home

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.*
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ItemHomeRV
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome
import grand.app.moon.presentation.home.viewModels.Home2ViewModel
import grand.app.moon.presentation.splash.postDelayedWithReceiverAndRunCatching
import grand.app.moon.presentation.splash.postWithReceiverAndRunCatching

fun Home2ViewModel.getMainAdapter(): RVItemCommonListUsageWithDifferentItems<ItemHomeRV> {
	return RVItemCommonListUsageWithDifferentItems(
		getItemIdBlock = { position ->
			position.toLong()
		},
		getLayoutRes = { position ->
			when (list[position].type) {
				ItemHomeRV.Type.STORIES -> R.layout.item_home_rv_for_stories
				ItemHomeRV.Type.CATEGORIES -> R.layout.item_home_rv_for_categories
				ItemHomeRV.Type.MOST_RATED_STORIES, ItemHomeRV.Type.FOLLOWING_STORIES -> R.layout.item_home_rv_for_stores
				else -> R.layout.item_home_rv_for_ads
			}
		},
		afterOnCreateViewHolder = { viewHolder, layoutRes, binding ->
			when (binding) {
				is ItemHomeRvForStoriesBinding -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						adapterStories,
						true,
						1
					) { layoutParams ->
						val number = 4
						val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

						val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

						layoutParams.width = (totalWidth - dpToPx8) / number
					}
				}
				is ItemHomeRvForCategoriesBinding -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						adapterCategories,
						true,
						1
					) { layoutParams ->
						val number = 4
						val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

						val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

						layoutParams.width = (totalWidth + layoutParams.marginEnd) / number
					}
				}
				is ItemHomeRvForStoresBinding -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						adapterOfStores,
						true,
						1
					) { layoutParams ->
						val number = 2
						val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

						val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

						layoutParams.width = totalWidth / number

						MyLogger.e("hhhhhhhhh -> ${layoutParams.height}")
					}
				}
				is ItemHomeRvForAdsBinding -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						adapterOfAds,
						true,
						1
					) { layoutParams ->
						val number = 2
						val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

						val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

						layoutParams.width = totalWidth / number
					}
				}
			}
		}
	) { binding, position, item ->
		MyLogger.e("on binddddddddddddddddddddddd $position")

		binding.root.tag = item.toJsonInlinedOrNull()

		when (binding) {
			is ItemHomeRvForStoriesBinding -> {
				binding.nameTextView.text = item.name

				binding.countTextView.text = item.count.toStringOrEmpty()
			}
			is ItemHomeRvForCategoriesBinding -> {
				binding.nameTextView.text = item.name

				binding.countTextView.text = item.count.toStringOrEmpty()
			}
			is ItemHomeRvForStoresBinding -> {
				binding.nameTextView.text = item.name

				binding.countTextView.text = item.count.toStringOrEmpty()

				(binding.recyclerView.adapter as? RVItemCommonListUsage<ItemHomeRvStoreBinding, ItemStoreInResponseHome>)?.also { adapter ->
					if (adapter.list.isNotEmpty()) return@also

					MyLogger.e("on binddddddddddddddddddddddd HEREEEE $position")

					val toBeUsedAdapter = when (item.type) {
						ItemHomeRV.Type.MOST_RATED_STORIES -> adapterMostRatedStore
						else /*ItemHomeRV.Type.FOLLOWING_STORIES*/ -> adapterFollowingsStores
					}

					adapter.submitList(toBeUsedAdapter.list)
				}
			}
			is ItemHomeRvForAdsBinding -> {
				binding.nameTextView.text = item.name

				binding.countTextView.text = item.count.toStringOrEmpty()

				(binding.recyclerView.adapter as? RVItemCommonListUsage<ItemHomeRvAdvBinding, ItemAdvertisementInResponseHome>)?.also { adapter ->
					if (adapter.list.isNotEmpty()) return@also

					MyLogger.e("on binddddddddddddddddddddddd HEREEEE $position")

					val index = position - adapterDynamicCategoryAdsStartIndex

					val toBeUsedAdapter = when (item.type) {
						ItemHomeRV.Type.SUGGESTED_ADS -> adapterSuggestedAds
						ItemHomeRV.Type.MOST_POPULAR_ADS -> adapterMostPopularAds
						else /*ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS*/ -> adapterDynamicCategoryAds[index]
					}

					adapter.submitList(toBeUsedAdapter.list)
				}
			}
		}
	}
}
