package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeRvStoryActualBinding
import grand.app.moon.domain.home.models.StoreModel
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.AllStoriesFragment
import grand.app.moon.presentation.home.AllStoriesFragmentDirections
import grand.app.moon.presentation.home.Home2FragmentDirections
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.models.toStore
import javax.inject.Inject

@HiltViewModel
class AllStoriesViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val showFollowingStories = MutableLiveData(true)

	val adapterFollowing = getAdapter()

	val adapterOther = getAdapter()

	val storiesFollowing = repoShop.getAllStoriesFollowing()

	val storiesOther = repoShop.getAllStoriesOther()

	private fun getAdapter() = RVPagingItemCommonListUsage<ItemHomeRvStoryActualBinding, ResponseStory>(
		R.layout.item_home_rv_story_actual,
		onItemClick = { adapter, binding ->
			val fragment = binding.root.findFragmentOrNull<AllStoriesFragment>() ?: return@RVPagingItemCommonListUsage

			val newList = adapter.snapshot().items.drop(1)

			val position = binding.root.tag as? Int ?: return@RVPagingItemCommonListUsage

			val storyModel = StoreModel()
			storyModel.list.addAll(newList.map { it.toStore() })
			storyModel.position = position - 1

			fragment.findNavController()
				.navigateSafely(AllStoriesFragmentDirections.actionDestAllStoriesToStoryFragment(storyModel))
		}
	) { binding, position, item ->
		binding.root.tag = position

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
