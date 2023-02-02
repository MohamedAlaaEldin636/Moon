package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.core.extenstions.inflateLayout
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
	var adapterDynamicCategoryAdsStartIndex = 0

	private val dpToPx9 by lazy { app.dpToPx(9f).roundToInt() }

	private val dpToPx8 by lazy { app.dpToPx(8f).roundToInt() }

	private val dpToPx16 by lazy { app.dpToPx(16f).roundToInt() }

	private val dpToPx103 by lazy { app.dpToPx(103f).roundToInt() }

	private fun RecyclerView.aaa() {
		clearOnScrollListeners()
		addOnScrollListener(object : OnScrollListener(){
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					post { isVisible = true }
				}
			}
		})
	}

	val adapter = RVItemCommonListUsage<ItemHomeRvBinding, ItemHomeRV>(
		R.layout.item_home_rv,
	) { binding, position, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.nameTextView.text = item.name

		binding.countTextView.text = item.count.toStringOrEmpty()

		binding.recyclerView.isVisible = false
		binding.helperView.isVisible = true
		// todo try adapter optimizations

		when (item.type) {
			ItemHomeRV.Type.STORIES -> {
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
			ItemHomeRV.Type.CATEGORIES -> {
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
			ItemHomeRV.Type.MOST_RATED_STORIES -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterMostRatedStore,
					true,
					1
				) { layoutParams ->
					val number = 2
					val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

					val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

					layoutParams.width = totalWidth / number
				}
			}
			ItemHomeRV.Type.FOLLOWING_STORIES -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterFollowingsStores,
					true,
					1
				) { layoutParams ->
					val number = 2
					val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

					val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

					layoutParams.width = totalWidth / number
				}
			}
			ItemHomeRV.Type.SUGGESTED_ADS -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterSuggestedAds,
					true,
					1
				) { layoutParams ->
					val number = 2
					val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

					val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

					layoutParams.width = totalWidth / number
				}
			}
			ItemHomeRV.Type.MOST_POPULAR_ADS -> {
				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterMostPopularAds,
					true,
					1
				) { layoutParams ->
					val number = 2
					val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

					val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

					layoutParams.width = totalWidth / number
				}
			}
			ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS -> {
				val index = position - adapterDynamicCategoryAdsStartIndex

				binding.recyclerView.setupWithRVItemCommonListUsage(
					adapterDynamicCategoryAds[index],
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

	fun onRefreshWholeScreen(view: View) {
		val fragment = view.findFragmentOrNull<Home2Fragment>() ?: return

		fragment.getWholeHomeData(false)
	}

	fun goToSearch(view: View) {
		TODO()
	}

}
