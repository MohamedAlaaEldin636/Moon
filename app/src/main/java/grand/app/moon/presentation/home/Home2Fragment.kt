package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentHome2Binding
import grand.app.moon.databinding.ItemHomeRvBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.models.ItemHomeRV
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.viewModels.Home2ViewModel

@AndroidEntryPoint
class Home2Fragment : BaseFragment<FragmentHome2Binding>() {

	private val viewModel by viewModels<Home2ViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (viewModel.repoShop.getCategoriesWithSubCategoriesAndBrands().isEmpty()) {
			handleRetryAbleActionOrGoBack(
				action = {
					viewModel.repoShop.getAllAppCategoriesWithSubcategoriesAndBrands()
				},
				hideLoadingCode = {}
			) { list ->
				viewModel.repoShop.saveAllAppCategoriesWithSubcategoriesAndBrandsLocally(list)

				viewModel.adapterCategories.submitList(list)

				loadStoriesAndPossiblyAds(showLoading = false, loadAds = true)
			}
		}else {
			loadStoriesAndPossiblyAds(showLoading = true, loadAds = true)
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_home_2

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		if (binding.recyclerView.adapter == null) {
			//binding.recyclerView.recycledViewPool.setMaxRecycledViews(0, Int.MAX_VALUE)

			binding.recyclerView.setupWithRVItemCommonListUsage(
				viewModel.adapter,
				false,
				1
			)
		}

		viewModel.adapterCategories.submitList(viewModel.repoShop.getCategoriesWithSubCategoriesAndBrands())
	}

	fun loadStoriesAndPossiblyAds(showLoading: Boolean, loadAds: Boolean) {
		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoHome2.getNotAllStories()
			},
			hideLoadingCode = {
				if (loadAds.not()) {
					_binding?.swipeRefreshLayout?.isRefreshing = false
				}
			},
			showLoadingCode = {
				if (showLoading && loadAds) showLoading()

				if (loadAds.not()) {
					_binding?.swipeRefreshLayout?.isRefreshing = true
				}
			}
		) { stories ->
			viewModel.adapterStories.submitList(listOf(ResponseStory()) + stories.sortedBy {
				if (it.isSeen) 1 else 0
			})

			if (loadAds) {
				handleRetryAbleActionOrGoBack(
					action = {
						viewModel.repoHome2.getHome()
					},
					showLoadingCode = {}
				) { responseHome ->
					viewModel.adapterMostRatedStore.submitList(responseHome.mostRatedStores.orEmpty())

					viewModel.adapterFollowingsStores.submitList(responseHome.followingsStores.orEmpty())

					viewModel.adapterSuggestedAds.submitList(responseHome.suggestedAds.orEmpty())

					viewModel.adapterMostPopularAds.submitList(responseHome.mostPopularAds.orEmpty())

					val dynamicCategoriesAds = responseHome.dynamicCategoriesAds.orEmpty().filter {
						it.advertisements.isNullOrEmpty().not()
					}
					viewModel.adapterDynamicCategoryAds = dynamicCategoriesAds.map { category ->
						viewModel.getAdapterForAds().also { it.submitList(category.advertisements.orEmpty()) }
					}

					val list = mutableListOf(
						ItemHomeRV(ItemHomeRV.Type.STORIES, null),
						ItemHomeRV(ItemHomeRV.Type.CATEGORIES, getString(R.string.departments)),
					)

					if (viewModel.adapterMostRatedStore.itemCount > 0) {
						list += ItemHomeRV(ItemHomeRV.Type.MOST_RATED_STORIES, getString(R.string.most_rated_stores))
					}
					if (viewModel.adapterFollowingsStores.itemCount > 0) {
						list += ItemHomeRV(ItemHomeRV.Type.FOLLOWING_STORIES, getString(R.string.following_stories))
					}
					if (viewModel.adapterSuggestedAds.itemCount > 0) {
						list += ItemHomeRV(ItemHomeRV.Type.SUGGESTED_ADS, getString(R.string.suggestions_ads_for_you))
					}
					if (viewModel.adapterMostPopularAds.itemCount > 0) {
						list += ItemHomeRV(ItemHomeRV.Type.MOST_POPULAR_ADS, getString(R.string.most_popular_ads))
					}

					viewModel.adapterDynamicCategoryAdsStartIndex = list.size
					if (dynamicCategoriesAds.isNotEmpty()) {
						for (item in dynamicCategoriesAds) {
							list += ItemHomeRV(ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS, item.name, item.adsCount, item.id)
						}
					}

					viewModel.adapter.submitList(list)
				}
			}
		}
	}
}
