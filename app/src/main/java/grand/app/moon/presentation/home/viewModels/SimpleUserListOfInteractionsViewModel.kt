package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSimpleUserBinding
import grand.app.moon.databinding.ItemUserInShopInfoStoriesOrExploresBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragment
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragmentArgs
import grand.app.moon.presentation.stats.models.ItemStoreStats
import javax.inject.Inject

@HiltViewModel
class SimpleUserListOfInteractionsViewModel @Inject constructor(
	application: Application,
	val args: SimpleUserListOfInteractionsFragmentArgs,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val showWholePageLoader = MutableLiveData(true)

	val users = when (args.type) {
		SimpleUserListOfInteractionsFragment.Type.EXPLORE_LIKES -> repoShop.getSimpleUsersOfExploreLikes(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.STORE_VIEWS -> repoShop.getStoreViews(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.STORE_FOLLOWERS -> repoShop.getStoreFollowers(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_LIKES -> repoShop.getStoryLikes(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_VIEWS -> repoShop.getStoryViews(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_SHARES -> repoShop.getStoryShares(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_EXPLORE_LIKES -> repoShop.getExploreLikes(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_EXPLORE_COMMENTS -> repoShop.getExploreComments(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_EXPLORE_SHARES -> repoShop.getExploreShares(args.exploreId)
	}

	val showDataTitle = when (args.type) {
		SimpleUserListOfInteractionsFragment.Type.EXPLORE_LIKES,
		SimpleUserListOfInteractionsFragment.Type.STORE_VIEWS,
		SimpleUserListOfInteractionsFragment.Type.STORE_FOLLOWERS -> true
		else -> false
	}

	val adapter = RVPagingItemCommonListUsageWithDifferentItems<SimpleUserListOfInteractionsFragment.Item>(
		getLayoutRes = {
			when (args.type) {
				SimpleUserListOfInteractionsFragment.Type.EXPLORE_LIKES,
				SimpleUserListOfInteractionsFragment.Type.STORE_VIEWS,
				SimpleUserListOfInteractionsFragment.Type.STORE_FOLLOWERS -> R.layout.item_simple_user
				else -> R.layout.item_user_in_shop_info_stories_or_explores
			}
		},
		onItemClick = { adapter, binding ->
			val item = binding.root.getTagJson<SimpleUserListOfInteractionsFragment.Item>()
				?: return@RVPagingItemCommonListUsageWithDifferentItems

			if (item.count != null && args.type == SimpleUserListOfInteractionsFragment.Type.STORE_VIEWS) {
				binding.root.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.stats.users.history.for.other.store",
					paths = arrayOf(item.name, ItemStoreStats.Type.VIEWS.apiValue, item.id.orZero().toString(), (-1).toString(), args.storeId.toString())
				)
			}
		},
		additionalListenersSetups = { _, binding ->
			if (binding is ItemUserInShopInfoStoriesOrExploresBinding) {
				binding.whatsappImageView.setOnClickListener { view ->
					val item = binding.root.getTagJson<SimpleUserListOfInteractionsFragment.Item>()
						?: return@setOnClickListener

					view.context?.launchWhatsApp(item.countryCode.orEmpty() + item.phone.orEmpty())
				}
			}
		}
	) { binding, _, item ->
		binding.root.setTagJson(item)

		when (binding) {
			is ItemSimpleUserBinding -> {
				binding.textView.text = item.name

				binding.imageView.setupWithGlide {
					load(item.image).saveDiskCacheStrategyAll()
				}

				binding.countTextView.isVisible = item.count != null
				binding.countTextView.text = item.count.toStringOrEmpty()
				binding.createdAtTextView.text = item.createdAt.orEmpty()
			}
			is ItemUserInShopInfoStoriesOrExploresBinding -> {
				binding.nameTextView.text = item.name

				binding.imageImageView.setupWithGlide {
					load(item.image).saveDiskCacheStrategyAll()
				}

				binding.countTextView.isVisible = item.count != null
				binding.countTextView.text = item.count.toStringOrEmpty()
				binding.dateTextView.text = item.createdAt.orEmpty()

				binding.emailValueTextView.text = item.email.orEmpty()
				binding.phoneValueTextView.text = "${item.countryCode.orEmpty()} ${item.phone.orEmpty()}"
			}
		}
	}

	// EXPLORE_LIKES
	// exploreId
	// pageTitle
	// dataTitle

	// getSimpleUsersOfExploreLikes



}
