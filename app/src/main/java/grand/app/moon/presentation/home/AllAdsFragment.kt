package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.FragmentAllAdsBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultFooterOnlyAdapter
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.AllAdsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class AllAdsFragment : BaseFragment<FragmentAllAdsBinding>() {

	companion object {
		fun launch(navController: NavController, filter: FilterAllFragment.Filter?, title: String) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.all.ads",
				paths = arrayOf(title, filter?.toSpecialString().orStringNullIfNullOrEmpty())
			)
		}
	}

	private val viewModel by viewModels<AllAdsViewModel>()

	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getAllAdsHandlingChanges(viewModel.filter.value ?: FilterAllFragment.Filter())
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

	override fun getLayoutId(): Int = R.layout.fragment_all_ads

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		retryAbleFlow.collectLatest()

		binding.recyclerViewCategories.setupWithRVItemCommonListUsage(
			viewModel.adapterCategories,
			true,
			1
		) { layoutParams ->
			val number = 4
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth + layoutParams.marginEnd) / number
		}

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
		}

		binding.rootConstraintLayout.post {
			val layoutParams = binding.recyclerViewAds.layoutParams
			layoutParams.height = binding.heightHelper2View.height.plus(
				binding.buttonsConstraintLayout.top
			).minus(
				context?.dpToPx(9.5f)?.roundToInt().orZero()
			)
			binding.recyclerViewAds.layoutParams = layoutParams
		}
	}

}
