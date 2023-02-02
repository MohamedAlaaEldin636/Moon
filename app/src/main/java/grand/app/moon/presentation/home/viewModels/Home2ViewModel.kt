package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.data.home2.RepoHome2
import grand.app.moon.databinding.ItemHomeRvAdvBinding
import grand.app.moon.databinding.ItemHomeRvBinding
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ItemHomeRV
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class Home2ViewModel @Inject constructor(
	application: Application,
	val repoHome2: RepoHome2,
	val repoHome: HomeUseCase,
) : AndroidViewModel(application) {

	val adapterStories by lazy { getAdapterStories() }

	val adapterCategories by lazy { getAdapterCategories() }

	val adapterMostRatedStore by lazy { getAdapterForStores() }

	val adapterFollowingsStores by lazy { getAdapterForStores() }

	val adapterSuggestedAds by lazy { getAdapterForAds() }

	val adapterMostPopularAds by lazy { getAdapterForAds() }

	var adapterDynamicCategoryAds = emptyList<RVItemCommonListUsage<ItemHomeRvAdvBinding, ItemAdvertisementInResponseHome>>()

	private val dpToPx9 by lazy { app.dpToPx(9f).roundToInt() }

	val adapter = RVItemCommonListUsage<ItemHomeRvBinding, ItemHomeRV>(
		R.layout.item_home_rv,
	) { binding, position, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.nameTextView.text = item.name

		binding.countTextView.text = item.count.toStringOrEmpty()

		when (item.type) {
			ItemHomeRV.Type.STORIES -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterStories,
					true,
					1
				) { layoutParams ->
					layoutParams.width = width / 4
				}
			}
			ItemHomeRV.Type.CATEGORIES -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterCategories,
					true,
					1
				) { layoutParams ->
					val margin = layoutParams.marginStart + layoutParams.marginEnd

					layoutParams.width = (width / 4) + (/*context.dpToPx(13f).roundToInt()*/margin / 4)
				}
			}
			ItemHomeRV.Type.MOST_RATED_STORIES -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterMostRatedStore,
					true,
					1
				) { layoutParams ->
					layoutParams.width = width / 2
				}
			}
			ItemHomeRV.Type.FOLLOWING_STORIES -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterFollowingsStores,
					true,
					1
				) { layoutParams ->
					layoutParams.width = width / 2
				}
			}
			ItemHomeRV.Type.SUGGESTED_ADS -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterSuggestedAds,
					true,
					1
				) { layoutParams ->
					val margin = layoutParams.marginStart + layoutParams.marginEnd

					layoutParams.width = (width / 2) - ((margin + dpToPx9) / 2)
				}
			}
			ItemHomeRV.Type.MOST_POPULAR_ADS -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterMostPopularAds,
					true,
					1
				) { layoutParams ->
					val margin = layoutParams.marginStart + layoutParams.marginEnd

					layoutParams.width = (width / 2) - ((margin + dpToPx9) / 2)
				}
			}
			ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS -> {

			}
		}
	}

	fun onRefreshWholeScreen(view: View) {
		val fragment = view.findFragmentOrNull<Home2Fragment>() ?: return

		fragment.getWholeHomeData(false)
	}

	fun goToSearch(view: View) {
		TODO()
	}

}
