package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAdsInteractedWithByOtherUsersBinding
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.extensions.toStringOrEmpty
import grand.app.moon.helpers.paging.withDefaultFooterOnlyAdapter
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.AdsInteractedWithByOtherUsersViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdsInteractedWithByOtherUsersFragment : BaseFragment<FragmentAdsInteractedWithByOtherUsersBinding>() {

	companion object {
		fun launch(navController: NavController, toolbarTitle: String, type: Type) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.ads.interacted.with.by.other.users",
				paths = arrayOf(toolbarTitle, type.toStringOrEmpty())
			)
		}
	}

	private val viewModel by viewModels<AdsInteractedWithByOtherUsersViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_ads_interacted_with_by_other_users

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter.withDefaultFooterOnlyAdapter(),
			false,
			1
		)

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.adapter.showLoadingFlow.collectLatest {
					viewModel.showWholePageLoader.value = it

					if (viewModel.showLabelText && it.not()) {
						viewModel.count.value = viewModel.adapter.snapshot().items.firstOrNull()
							?.totalCountInPagination.orZero()
					}
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.ads.collectLatest {
					viewModel.adapter.submitData(it)
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.ads.collectLatest {
					viewModel.adapter.submitData(it)
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.adapter.showEmptyViewFlow.collectLatest {
					viewModel.showEmptyView.value = it
				}
			}
		}
	}

	enum class Type {
		LAST_VIEWED, LAST_SEARCHED, FAV
	}

}
