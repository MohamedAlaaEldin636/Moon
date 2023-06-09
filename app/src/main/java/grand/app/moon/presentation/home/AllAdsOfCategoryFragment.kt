package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.FragmentAllAdsOfCategoryBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultFooterOnlyAdapter
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.AllAdsOfCategoryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class AllAdsOfCategoryFragment : BaseFragment<FragmentAllAdsOfCategoryBinding>() {

	companion object {
		fun launch(
			navController: NavController,
			filter: FilterAllFragment.Filter?,
			title: String,
			categoryId: Int
		) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.all.ads.of.category",
				paths = arrayOf(
					title,
					filter?.toSpecialString().orStringNullIfNullOrEmpty(),
					categoryId.toString()
				)
			)
		}
	}

	private val viewModel by viewModels<AllAdsOfCategoryViewModel>()

	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getAllAdsOfCategory(viewModel.filter.value ?: FilterAllFragment.Filter())
			},
			collectLatestAction = {
				viewModel.adapterAds.submitData(it)
			},
			onRetry = {
				kotlin.runCatching { viewModel.adapterAds.submitData(viewLifecycleOwner.lifecycle, PagingData.empty()) }

				viewModel.adapterAds.refresh()
			}
		)
	}

	override fun getLayoutId(): Int = R.layout.fragment_all_ads_of_category

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		retryAbleFlow.collectLatest()

		binding.recyclerViewSubCategories.setupWithRVItemCommonListUsage(
			viewModel.adapterSubCategories,
			true,
			1
		)

		binding.recyclerViewAds.setupWithRVItemCommonListUsage(
			viewModel.adapterAds.withDefaultFooterOnlyAdapter(),
			false,
			2,
			onGridLayoutSpanSizeLookup = {
				if (viewModel.layoutIsTwoColNotOneCol.value == true) 1 else 2
			}
		)

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.adapterAds.showLoadingFlow.collectLatest {
					viewModel.showWholePageLoader.value = it
				}
			}
		}

		viewModel.layoutIsTwoColNotOneCol.distinctUntilChanged().ignoreFirstTimeChanged().observe(viewLifecycleOwner) {
			binding.recyclerViewAds.layoutManager?.requestLayout()
		}

		binding.searchEditText.setOnEditorActionListenerBA(viewModel.getOnEditorListener())

		viewModel.filter.observe(viewLifecycleOwner) {
			retryAbleFlow.retry()

			val toolbarName = viewModel.allCategories.firstOrNull {
				it.id == viewModel.filter.value?.categoryId
			}?.name.letIfNullOrEmpty { viewModel.args.title }
			(activity as? HomeActivity)?.binding?.toolbar?.title = toolbarName
		}

		binding.rootConstraintLayout.post {
			val layoutParams = binding.recyclerViewAds.layoutParams
			layoutParams.height = binding.heightRVView.height.plus(
				binding.buttonsConstraintLayout.top
			).minus(
				context?.dpToPx(9.5f)?.roundToInt().orZero()
			)
			binding.recyclerViewAds.layoutParams = layoutParams
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.adapterAds.loadStateFlow.collectLatest {
					viewModel.adapterAds.snapshot().items.firstOrNull().also { item ->
						if (viewModel.numOfAds.value != item?.adsCount) {
							viewModel.numOfAds.value = item?.adsCount.orZero()
						}

						viewModel.showSlider.value = item?.slider.isNullOrEmpty().not()
						if (viewModel.adapterSlider.list != item?.slider) {
							viewModel.adapterSlider.submitList(
								item?.slider.orEmpty()
							)

							binding.sliderView.post {
								binding.sliderView.setSliderAdapter(viewModel.adapterSlider)
								binding.sliderView.startAutoCycle()
							}
						}
					}
				}
			}
		}
	}

}
