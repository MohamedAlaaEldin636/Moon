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
import grand.app.moon.databinding.FragmentFollowedStoresBinding
import grand.app.moon.extensions.RetryAbleFlow
import grand.app.moon.extensions.ignoreFirstTimeChanged
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.helpers.paging.withDefaultFooterOnlyAdapter
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.FollowedStoresViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowedStoresFragment : BaseFragment<FragmentFollowedStoresBinding>() {

	companion object {
		fun launch(navController: NavController) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.followed.stores"
			)
		}
	}

	private val viewModel by viewModels<FollowedStoresViewModel>()

	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getFollowedStores(viewModel.selectedCategory.value?.id)
			},
			collectLatestAction = {
				viewModel.adapterStores.submitData(it)
			},
			onRetry = {
				kotlin.runCatching { viewModel.adapterStores.submitData(viewLifecycleOwner.lifecycle, PagingData.empty()) }

				viewModel.adapterStores.refresh()
			}
		)
	}

	override fun getLayoutId(): Int = R.layout.fragment_followed_stores

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		retryAbleFlow.collectLatest()

		viewModel.selectedCategory.distinctUntilChanged().ignoreFirstTimeChanged().observe(viewLifecycleOwner) {
			retryAbleFlow.retry()
		}

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

		binding.recyclerViewStores.setupWithRVItemCommonListUsage(
			viewModel.adapterStores.withDefaultFooterOnlyAdapter(),
			false,
			1
		)

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.adapterStores.showLoadingFlow.collectLatest {
					viewModel.showWholePageLoader.value = it
				}
			}
		}
	}

	fun reFetchData() {
		retryAbleFlow.retry()
	}

}
