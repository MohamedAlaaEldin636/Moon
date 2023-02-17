package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.data.home2.RepoHome2
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeRvAdvBinding
import grand.app.moon.databinding.ItemHomeRvBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.utils.Constants
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
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase
) : AndroidViewModel(application) {

	val adapterStories by lazy { getAdapterStories() }

	val adapterCategories by lazy { getAdapterCategories() }

	val adapterMostRatedStore by lazy { getAdapterForStores(true) }

	val adapterFollowingsStores by lazy { getAdapterForStores(false) }

	val adapterSuggestedAds by lazy { getAdapterForAds() }

	val adapterMostPopularAds by lazy { getAdapterForAds() }

	var adapterDynamicCategoryAds = emptyList<RVItemCommonListUsage<ItemHomeRvAdvBinding, ItemAdvertisementInResponseHome>>()
	var adapterDynamicCategoryAdsStartIndex = 0

	private val dpToPx9 by lazy { app.dpToPx(9f).roundToInt() }

	private val dpToPx8 by lazy { app.dpToPx(8f).roundToInt() }

	private val dpToPx16 by lazy { app.dpToPx(16f).roundToInt() }

	private val dpToPx103 by lazy { app.dpToPx(103f).roundToInt() }

	private val dpToPx104 by lazy { app.dpToPx(104f).roundToInt() }
	private val dpToPx119 by lazy { app.dpToPx(119f).roundToInt() }
	private val dpToPx281 by lazy { app.dpToPx(281f).roundToInt() }
	private val dpToPx269 by lazy { app.dpToPx(269f).roundToInt() } // adv
	private var heightStore = 0
	private var heightAdv = 0

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
		additionalListenersSetups = { adapter, binding ->
			binding.showAllTextView.setOnClickListener { view ->
				val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemHomeRV>()
					?: return@setOnClickListener

				val navController = view.findNavController()

				when (item.type) {
					ItemHomeRV.Type.STORIES -> {
						navController.navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest.all.stories"
						)
					}
					ItemHomeRV.Type.CATEGORIES -> {
						navController.navigateSafely(
							Home2FragmentDirections.actionDestHomeToDepartmentListFragment()
						)
					}
					ItemHomeRV.Type.MOST_RATED_STORIES -> {
						navController.navigateSafely(
							Home2FragmentDirections.actionDestHomeFragmentToStoreListFragment(
								view.resources.getString(R.string.top_stores_rated),
								3
							)
						)
					}
					ItemHomeRV.Type.FOLLOWING_STORIES -> {
						navController.navigateDeepLinkWithOptions(
							"store",
							"grand.app.moon.store.followed"
						)
					}
					ItemHomeRV.Type.SUGGESTED_ADS -> {
						navController.navigate(
							R.id.nav_category_list_ads,
							bundleOf(
								"category_id" to -1,
								"tabBarText" to item.name.orEmpty(),
								"type" to 1
							), Constants.NAVIGATION_OPTIONS
						)
					}
					ItemHomeRV.Type.MOST_POPULAR_ADS -> {
						navController.navigate(
							R.id.nav_category_list_ads,
							bundleOf(
								"category_id" to -1,
								"tabBarText" to item.name.orEmpty(),
								"type" to 2
							), Constants.NAVIGATION_OPTIONS
						)
					}
					ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS -> {
						navController.navigate(
							R.id.nav_category_list_ads,
							bundleOf(
								"category_id" to item.dynamicCategoriesAdsId.orZero(),
								"tabBarText" to item.name.orEmpty()
							), Constants.NAVIGATION_OPTIONS
						)
					}
				}
			}
		}
	) { binding, position, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.root.tag = item.toJsonInlinedOrNull()

		binding.nameTextView.text = item.name

		binding.countTextView.text = item.count.toStringOrEmpty()

		binding.recyclerView.adapter = null
		binding.helperView.visibility = View.VISIBLE
		val rv = binding.recyclerView
		val layoutParams = binding.recyclerView.layoutParams as? ConstraintLayout.LayoutParams
			?: return@RVItemCommonListUsage
		val (width, height) = when (item.type) {
			ItemHomeRV.Type.STORIES -> {
				val number = 4
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = rv.width - rv.paddingStart - rv.paddingEnd - (number.dec() * itemMargins)

				val width = (totalWidth - dpToPx8) / number

				width to dpToPx119
			}
			ItemHomeRV.Type.CATEGORIES -> {
				val number = 4
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = rv.width - rv.paddingStart - rv.paddingEnd - (number.dec() * itemMargins)

				val width = (totalWidth + layoutParams.marginEnd) / number

				width to dpToPx119
			}
			ItemHomeRV.Type.MOST_RATED_STORIES, ItemHomeRV.Type.FOLLOWING_STORIES -> {
				val number = 2
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = rv.width - rv.paddingStart - rv.paddingEnd - (number.dec() * itemMargins)

				val width = totalWidth / number

				width to if (heightStore == 0) dpToPx281 else heightStore
			}
			else -> {
				val number = 2
				val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

				val totalWidth = rv.width - rv.paddingStart - rv.paddingEnd - (number.dec() * itemMargins)

				val width = totalWidth / number

				width to if (heightAdv == 0) dpToPx269 else heightAdv
			}
		}
		val helperLayoutParams = binding.helperView.layoutParams as? ConstraintLayout.LayoutParams
			?: return@RVItemCommonListUsage
		helperLayoutParams.width = width
		helperLayoutParams.height = height
		binding.helperView.layoutParams = helperLayoutParams
	}

	fun onRefreshWholeScreen(view: View) {
		val fragment = view.findFragmentOrNull<Home2Fragment>() ?: return

		fragment.loadStoriesAndPossiblyAds(showLoading = true, loadAds = false)
	}

	fun goToSearch(view: View) {
		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.search.suggestions"
		)
	}

	fun goToFilter(view: View) {
		/*if (true) {
			view.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.old.home"
			)

			return
		}*/

		toFilter(view)
	}

	fun toFilter(
		v: View, category_id: Int? = -1, category_name: String? = null, sub_category_id: Int? = -1,
		sub_category_name: String? = null, allow_change_category: Boolean = true, store_id: Int = -1,
		store: Store? = Store()
	) {
		val bundle = Bundle()

		category_id?.let { bundle.putInt("category_id", it) }
		if (sub_category_id != null) {
			bundle.putInt("sub_category_id", sub_category_id)
		}
		bundle.putInt("store_id", store_id)
		bundle.putBoolean("allow_change_category", allow_change_category)

		category_name?.let {
			bundle.putString("category_name", it)
		}

		sub_category_name?.let {
			bundle.putString("sub_category_name", it)
		}

		store?.let {
			bundle.putSerializable("store", it)
		}


		v.findNavController()
			.navigate(R.id.to_filter, bundle, Constants.NAVIGATION_OPTIONS)
	}

	fun setupRvs(binding: ItemHomeRvBinding, item: ItemHomeRV, position: Int) {
		binding.helperView.visibility = View.INVISIBLE

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

					MyLogger.e("hhhhhhhhh -> ${layoutParams.height}")
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

}
