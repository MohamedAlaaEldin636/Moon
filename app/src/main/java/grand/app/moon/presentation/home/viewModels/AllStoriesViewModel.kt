package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeRvStoryActualBinding
import grand.app.moon.domain.home.models.StoreModel
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.visibleOrInvisible
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
			//val context = fragment.context ?: return@RVPagingItemCommonListUsage

			val position = binding.root.tag as? Int ?: return@RVPagingItemCommonListUsage

			val storyModel = StoreModel()
			storyModel.list.addAll(adapter.snapshot().items.map { it.toStore() })
			storyModel.position = position

			if (true) {
				fragment.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.story.player",
					paths = arrayOf(
						adapter.snapshot().items.toJsonInlinedOrNull().orEmpty(),
						position.toString()
					)
				)
			}else {
				fragment.findNavController()
					.navigateSafely(Home2FragmentDirections.actionDestHome2ToStoryFragment(storyModel))
			}
		}
	) { binding, position, item ->
		MyLogger.e("aaaaaaaa $position")
		binding.root.tag = position

		binding.seenCircleView.visibleOrInvisible(
			item.isSeen.not()
		)

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
