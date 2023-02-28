package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.*
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.models.ItemHomeRV
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.viewModels.Home2ViewModel
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Home2Fragment : BaseFragment<FragmentHome2Binding>() {

	val viewModel by viewModels<Home2ViewModel>()

	private val activityViewModel by activityViewModels<HomeViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.callApi = true
	}

	private fun callApi() {
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

		if (viewModel.callApi) {
			viewModel.callApi = false

			callApi()
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
		) { response ->
			val souqMoonStory: ResponseStory? = response.souqMoonStory?.let {
				if (it.stories.isNullOrEmpty()) null else it.copy(isSouqMoonStory = true)
			}

			val allStories = listOfNotNull(souqMoonStory) + response.stories.orEmpty().sortedBy {
				if (it.isSeen) 1 else 0
			}

			viewModel.adapterStories.submitList(listOf(ResponseStory()) + allStories)

			if (loadAds) {
				if (_binding == null) return@handleRetryAbleActionOrGoBack

				binding.rvLikeNestedScrollView.post {
					kotlin.runCatching {
						binding.rvLikeLinearLayout.removeAllViews()
					}.getOrElse {
						MyLogger.e("error hudisaud $it")
					}

					handleRetryAbleActionOrGoBack(
						action = {
							viewModel.repoHome2.getHome()
						},
						showLoadingCode = {},
						onErrorCode = { failure, selfBlock ->
							showLoading()

							showRetryErrorDialogWithBackNegativeButton(
								failure.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
							) {
								selfBlock()
							}
						}
					) { responseHome ->
						activityViewModel.notificationsCount.value = responseHome.notificationCount.orZero()

						viewModel.listOfMostRatedStore = responseHome.mostRatedStores.orEmpty()
						viewModel.adapterMostRatedStore.submitList(responseHome.mostRatedStores.orEmpty())

						viewModel.listOfFollowingsStores = responseHome.followingsStores.orEmpty()
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

						lifecycleScope.launch {
							for ((position, item) in list.withIndex()) {
								val binding = DataBindingUtil.inflate<ItemHomeRvBinding>(
									layoutInflater, R.layout.item_home_rv, binding.rvLikeLinearLayout, false
								)

								binding.nameTextView.text = if (item.dynamicCategoriesAdsId == null) item.name else {
									"${getString(R.string.advertisements)} ${item.name}"
								}

								binding.countTextView.text = item.count.toStringOrEmpty()

								binding.recyclerView.setupInnerRvs(position, item.type)

								binding.showAllTextView.setupInnerShowAll(item.type, item)

								kotlin.runCatching {
									this@Home2Fragment.binding.rvLikeLinearLayout.addView(
										binding.root,
										LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
									)
								}.getOrElse {
									MyLogger.e("errorrrr uiheiwufhwe $it")
								}

								if (position.inc() % 3 == 0) {
									delay(500)
								}
							}
						}
					}
				}
			}
		}
	}

	private fun TextView.setupInnerShowAll(type: ItemHomeRV.Type, item: ItemHomeRV) {
		setOnClickListener { view ->
			//val context = view.context ?: return@setOnClickListener

			val navController = findNavController()

			when (type) {
				ItemHomeRV.Type.STORIES -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.all.stories"
					)
				}
				ItemHomeRV.Type.CATEGORIES -> {
					view.findNavController().navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.all.categories",
					)
					/*navController.navigateSafely(
						Home2FragmentDirections.actionDestHomeToDepartmentListFragment()
					)*/
				}
				ItemHomeRV.Type.MOST_RATED_STORIES -> {
					AllStoresFragment.launch(
						navController,
						AllStoresFragment.Filter(sortBy = AllStoresFragment.SortBy.HIGHEST_RATED),
						getString(R.string.stores_879)
					)
				}
				ItemHomeRV.Type.FOLLOWING_STORIES -> {
					navController.navigateDeepLinkWithOptions(
						"store",
						"grand.app.moon.store.followed"
					)
				}
				ItemHomeRV.Type.SUGGESTED_ADS -> {
					AllAdsFragment.launch(
						navController,
						FilterAllFragment.Filter(
							adSpecificType = FilterAllFragment.AdSpecificType.SUGGESTED
						),
						getString(R.string.suggestions_ads_for_you)
					)
				}
				ItemHomeRV.Type.MOST_POPULAR_ADS -> {
					AllAdsFragment.launch(
						navController,
						FilterAllFragment.Filter(
							adSpecificType = FilterAllFragment.AdSpecificType.MOST_POPULAR
						),
						getString(R.string.most_popular_ads)
					)
				}
				ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS -> {
					AllAdsOfCategoryFragment.launch(
						navController,
						null,
						item.name.orEmpty(),
						item.dynamicCategoriesAdsId.orZero(),
					)
				}
			}
		}
	}

	private fun RecyclerView.setupInnerRvs(position: Int, type: ItemHomeRV.Type) {
		when (type) {
			ItemHomeRV.Type.STORIES -> setupWithRVItemCommonListUsage(
				viewModel.adapterStories,
				true,
				1
			) { layoutParams ->
				val number = 4
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = (totalWidth - viewModel.dpToPx8) / number
			}
			ItemHomeRV.Type.CATEGORIES -> setupWithRVItemCommonListUsage(
				viewModel.adapterCategories,
				true,
				1
			) { layoutParams ->
				val number = 4
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = (totalWidth + layoutParams.marginEnd) / number
			}
			ItemHomeRV.Type.MOST_RATED_STORIES -> setupWithRVItemCommonListUsage(
				viewModel.adapterMostRatedStore,
				true,
				1
			) { layoutParams ->
				val number = 2
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = totalWidth / number

				MyLogger.e("hhhhhhhhh -> ${layoutParams.height}")
			}
			ItemHomeRV.Type.FOLLOWING_STORIES -> setupWithRVItemCommonListUsage(
				viewModel.adapterFollowingsStores,
				true,
				1
			) { layoutParams ->
				val number = 2
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = totalWidth / number

				MyLogger.e("hhhhhhhhh -> ${layoutParams.height}")
			}
			ItemHomeRV.Type.SUGGESTED_ADS -> setupWithRVItemCommonListUsage(
				viewModel.adapterSuggestedAds,
				true,
				1
			) { layoutParams ->
				val number = 2
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = totalWidth / number
			}
			ItemHomeRV.Type.MOST_POPULAR_ADS -> setupWithRVItemCommonListUsage(
				viewModel.adapterMostPopularAds,
				true,
				1
			) { layoutParams ->
				val number = 2
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = totalWidth / number
			}
			ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS -> setupWithRVItemCommonListUsage(
				viewModel.adapterDynamicCategoryAds[position - viewModel.adapterDynamicCategoryAdsStartIndex],
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
