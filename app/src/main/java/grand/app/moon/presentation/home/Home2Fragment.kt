package grand.app.moon.presentation.home

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.databinding.*
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.models.ItemHomeRV
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.viewModels.Home2ViewModel
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import kotlinx.coroutines.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class Home2Fragment : BaseFragment<FragmentHome2Binding>(), PermissionsHandler.Listener {

	val viewModel by viewModels<Home2ViewModel>()

	private val activityViewModel by activityViewModels<HomeViewModel>()

	private var permissionsHandler: PermissionsHandler? = null

	override fun onAllPermissionsAccepted() {
		// Do nothing used just for API 33 to be able to post notifications isa.
	}

	override fun onSubsetPermissionsAccepted(permissions: Map<String, Boolean>) {
		permissionsHandler?.actOnAllPermissionsAcceptedOrRequestPermissions()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			permissionsHandler = PermissionsHandler(
				this,
				lifecycle,
				requireContext(),
				listOf(
					Manifest.permission.POST_NOTIFICATIONS,
				),
				this
			)
		}

		super.onCreate(savedInstanceState)

		viewModel.callApi = true
	}

	override fun onResume() {
		super.onResume()

		permissionsHandler?.actOnAllPermissionsAcceptedOrRequestPermissions()
	}

	private fun callApi() {
		lifecycleScope.launch {
			val app = activity?.application as? MyApplication
			MyLogger.e("announc announc announc announc $app $activity ${activity?.application} ${app?.checkedAppGlobalAnnouncement}")
			if (app != null && !app.checkedAppGlobalAnnouncement) {
				val announcement = viewModel.repoShop.getAnnouncementIfShouldAppearOrNull()
				app.checkedAppGlobalAnnouncement = true

				MyLogger.e("announc announc announc announc $announcement")

				if (announcement != null) {
					kotlin.runCatching {
						findNavController().navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest.announcement.dialog",
							paths = arrayOf(announcement.image.orEmpty())
						)
					}.getOrElse {
						MyLogger.e("announc announc announc announc $it")
					}
				}
			}
		}

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

	override fun onDestroyView() {
		if (MyApplication.usedDeepLink.not()) {
			mRootView = null
			MyApplication.usedDeepLink = true
		}

		super.onDestroyView()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val context = context ?: return

		MyLogger.e("Home2Fragment -> onViewCreated ${viewModel.callApi}")
		if (MyApplication.usedDeepLink.not() && MyApplication.deepLinkUri != null) {
			val path = MyApplication.deepLinkUri?.path.toStringOrEmpty()
			MyLogger.e("dijasodjasoidjas $path")
			if (path.isNotEmpty()) {
				when {
					"shop" in path || "stores" in path -> {
						//https://om.sooqmoon.net/website/ar/shop/7779/mariz-store?story=view
						//https://om.sooqmoon.net/website/ar/shop/7779/mariz-store
						val id = MyApplication.deepLinkUri?.pathSegments?.dropLast(1)?.lastOrNull()?.toIntOrNull()

						if (id != null) {
							viewModel.userLocalUseCase.goToStoreDetailsIgnoringStoriesCheckIfMyStore(
								requireContext(),
								findNavController(),
								id
							)
						}
					}
					"explore" in path -> {
						//https://EG.sooqmoon.net/website/ar/explore/133
						val id = MyApplication.deepLinkUri?.pathSegments?.lastOrNull()?.toIntOrNull()

						if (id != null) {
							handleRetryAbleActionOrGoBack(
								action = {
									viewModel.repoShop.getExploreDetails(id)
								}
							) { item ->
								findNavController().navigateDeepLinkWithOptions(
									"fragment-dest",
									"grand.app.moon.dest.home.explore.subsection",
									paths = arrayOf(listOf(item).toJsonInlinedOrNull(), (-1).toString())
								)
							}
						}

						MyApplication.deepLinkUri = null
					}
					else -> {
						// https://OM.sooqmoon.net/website/ar/37880/ggg?store_id=983
						val id = MyApplication.deepLinkUri?.pathSegments?.dropLast(1)?.lastOrNull()?.toIntOrNull()

						if (id != null) {
							viewModel.userLocalUseCase.goToAdvDetailsCheckIfMyAdv(
								requireContext(),
								findNavController(),
								id,
								context.isLogin() && MyApplication.deepLinkUri
									?.getQueryParameter("store_id") == viewModel.userLocalUseCase().id.toString()
							)

							MyApplication.deepLinkUri = null
						}
					}
				}
			}
		}else {
			if (viewModel.callApi) {
				viewModel.callApi = false

				callApi()
			}

			viewModel.adapterCategories.submitList(viewModel.repoShop.getCategoriesWithSubCategoriesAndBrands())

			viewModel.onStoresFollowingStateChanges(this, context.getStoresFollowingStateChangesOnceTillNewChangesCome())
		}
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
				MyLogger.e("_binding $_binding")
				if (_binding == null) return@handleRetryAbleActionOrGoBack

				binding.rvLikeNestedScrollView.post {
					kotlin.runCatching {
						binding.rvLikeLinearLayout.removeAllViews()
						MyLogger.e("_binding removed all views")
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

								binding.countTextView.text = item.count.toStringOrEmpty().let {
									if (it.isEmpty()) it else "( $it )"
								}

								binding.recyclerView.setupInnerRvs(position, item.type)

								binding.showAllTextView.setupInnerShowAll(item.type, item)

								kotlin.runCatching {
									this@Home2Fragment.binding.rvLikeLinearLayout.addView(
										binding.root,
										LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
									)
									MyLogger.e("_binding adding a view")
								}.getOrElse {
									MyLogger.e("errorrrr uiheiwufhwe $it")
								}

								if (position.inc() % 3 == 0) {
									delay(500)
								}
							}

							//this@Home2Fragment.binding.rvLikeLinearLayout.requestLayout()

							//this@Home2Fragment.showMessage("dkapsdkp")

//							this@Home2Fragment.binding.rvLikeLinearLayout.addView(
//								TextView(requireContext()).also { it.text = "kewpodkwepdk" }
//							)
						}
					}
				}
			}
		}
	}

	fun TextView.setupInnerShowAll(type: ItemHomeRV.Type, item: ItemHomeRV) {
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
				}
				ItemHomeRV.Type.MOST_RATED_STORIES -> {
					AllStoresFragment.launch(
						navController,
						AllStoresFragment.Filter(sortBy = AllStoresFragment.SortBy.HIGHEST_RATED),
						getString(R.string.stores_879)
					)
				}
				ItemHomeRV.Type.FOLLOWING_STORIES -> {
					FollowedStoresFragment.launch(navController)
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
						FilterAllFragment.Filter(
							categoryId = item.dynamicCategoriesAdsId.orZero()
						),
						item.name.orEmpty(),
						item.dynamicCategoriesAdsId.orZero(),
					)
				}
			}
		}
	}

	fun RecyclerView.setupInnerRvs(position: Int, type: ItemHomeRV.Type) {
		when (type) {
			ItemHomeRV.Type.STORIES -> setupWithRVItemCommonListUsage(
				viewModel.adapterStories,
				true,
				1
			) { layoutParams ->
				val number = 3.75f
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = ((totalWidth - viewModel.dpToPx8.toFloat()) / number).roundToInt()
			}
			ItemHomeRV.Type.CATEGORIES -> setupWithRVItemCommonListUsage(
				viewModel.adapterCategories,
				true,
				1
			) { layoutParams ->
				val number = 3.1f
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

				layoutParams.width = ((totalWidth + layoutParams.marginEnd.toFloat()) / number).roundToInt()
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
