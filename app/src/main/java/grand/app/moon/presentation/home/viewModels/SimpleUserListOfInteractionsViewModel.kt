package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSimpleUserBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragment
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragmentArgs
import javax.inject.Inject

@HiltViewModel
class SimpleUserListOfInteractionsViewModel @Inject constructor(
	application: Application,
	val args: SimpleUserListOfInteractionsFragmentArgs,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val users = when (args.type) {
		SimpleUserListOfInteractionsFragment.Type.EXPLORE_LIKES -> repoShop.getSimpleUsersOfExploreLikes(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.STORE_VIEWS -> repoShop.getStoreViews(args.exploreId)
		SimpleUserListOfInteractionsFragment.Type.STORE_FOLLOWERS -> repoShop.getStoreFollowers(args.exploreId)
	}

	val adapter = RVPagingItemCommonListUsage<ItemSimpleUserBinding, SimpleUserListOfInteractionsFragment.Item>(
		R.layout.item_simple_user
	) { binding, _, item ->
		binding.textView.text = item.name

		binding.imageView.setupWithGlide {
			load(item.image).saveDiskCacheStrategyAll()
		}

		binding.countTextView.isVisible = item.count != null
		binding.countTextView.text = item.count.toStringOrEmpty()
		binding.createdAtTextView.text = item.createdAt.orEmpty()
	}

	// EXPLORE_LIKES
	// exploreId
	// pageTitle
	// dataTitle

	// getSimpleUsersOfExploreLikes



}
