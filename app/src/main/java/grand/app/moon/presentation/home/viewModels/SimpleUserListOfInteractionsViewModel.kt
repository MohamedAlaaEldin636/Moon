package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSimpleUserBinding
import grand.app.moon.extensions.RVPagingItemCommonListUsage
import grand.app.moon.extensions.asVideo
import grand.app.moon.extensions.saveDiskCacheStrategyAll
import grand.app.moon.extensions.setupWithGlide
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragment
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragmentArgs
import grand.app.moon.presentation.home.models.ResponseSimpleUserData
import javax.inject.Inject

@HiltViewModel
class SimpleUserListOfInteractionsViewModel @Inject constructor(
	application: Application,
	val args: SimpleUserListOfInteractionsFragmentArgs,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val users = when (args.type) {
		SimpleUserListOfInteractionsFragment.Type.EXPLORE_LIKES -> repoShop.getSimpleUsersOfExploreLikes(args.exploreId)
	}

	val adapter = RVPagingItemCommonListUsage<ItemSimpleUserBinding, ResponseSimpleUserData>(
		R.layout.item_simple_user
	) { binding, position, item ->
		binding.textView.text = item.name.orEmpty()

		binding.imageView.setupWithGlide {
			load(item.image).saveDiskCacheStrategyAll()
		}
	}

	// EXPLORE_LIKES
	// exploreId
	// pageTitle
	// dataTitle

	// getSimpleUsersOfExploreLikes



}
