package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAllAdsBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.viewModels.AllAdsViewModel

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
				viewModel.repoShop.getAllAds(viewModel.filter.value ?: FilterAllFragment.Filter())
			},
			collectLatestAction = {
				viewModel.adapterAds.submitData(it)
			},
			onRetry = {
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
			viewModel.adapterAds.withDefaultHeaderAndFooterAdapters(),
			false,
			2,
			onGridLayoutSpanSizeLookup = {
				if (viewModel.layoutIsTwoColNotOneCol.value == true) 1 else 2
			}
		)

		viewModel.layoutIsTwoColNotOneCol.distinctUntilChanged().ignoreFirstTimeChanged().observe(viewLifecycleOwner) {
			binding.recyclerViewAds.layoutManager?.requestLayout()
		}

		binding.searchEditText.setOnEditorActionListenerBA(viewModel.getOnEditorListener())

		viewModel.filter.observe(viewLifecycleOwner) {
			retryAbleFlow.retry()
		}

		binding.rootConstraintLayout.post {
			val layoutParams = binding.recyclerViewAds.layoutParams
			layoutParams.height = binding.rootConstraintLayout.height
			binding.recyclerViewAds.layoutParams = layoutParams
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<String>(QRCodeScannerFragment::class.java.name) {
			// Example -> https://eg.sooqmoon.net/ar/shop/9852/m1006mmm

			val endIndex = it.lastIndexOf("/")
			val id = if (endIndex.dec() < 0) null else {
				val index = it.lastIndexOf("/", startIndex = endIndex.dec())
				if (index.inc() < 0) null else it.substring(index.inc(), endIndex).toIntOrNull()
			}

			if (id == null) {
				showMessage(getString(R.string.something_went_wrong_please_try_again))

				return@observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull
			}

			context?.also { context ->
				viewModel.userLocalUseCase.goToStoreDetailsIgnoringStoriesCheckIfMyStore(
					context,
					findNavController(),
					id
				)
			}
		}
	}

}
