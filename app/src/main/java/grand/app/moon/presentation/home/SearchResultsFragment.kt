package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentSearchResultsBinding
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.helpers.paging.withDefaultHeaderOnlyAdapter
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.models.TypeSearchResult
import grand.app.moon.presentation.home.viewModels.SearchResultsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultsFragment : BaseFragment<FragmentSearchResultsBinding>() {

	private val viewModel by viewModels<SearchResultsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_search_results

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapterAdvertisements,
			false,
			1
		)

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					viewModel.advertisements.collectLatest {
						viewModel.adapterAdvertisements.submitData(it)
					}
				}
				launch {
					viewModel.stories.collectLatest {
						viewModel.adapterStories.submitData(it)
					}
				}
				launch {
					viewModel.nicknames.collectLatest {
						viewModel.adapterNicknames.submitData(it)
					}
				}
				launch {
					viewModel.categories.collectLatest {
						viewModel.adapterCategories.submitData(it)
					}
				}

				launch {
					viewModel.adapterAdvertisements.showEmptyViewFlow.collectLatest {
						viewModel.showEmptyForAds.value = it
					}
				}
				launch {
					viewModel.adapterStories.showEmptyViewFlow.collectLatest {
						viewModel.showEmptyForStores.value = it
					}
				}
				launch {
					viewModel.adapterNicknames.showEmptyViewFlow.collectLatest {
						viewModel.showEmptyForNicknames.value = it
					}
				}
				launch {
					viewModel.adapterCategories.showEmptyViewFlow.collectLatest {
						viewModel.showEmptyForCategories.value = it
					}
				}

				launch {
					viewModel.adapterAdvertisements.showLoadingFlow.collectLatest {
						viewModel.showLoadingForAds.value = it
					}
				}
				launch {
					viewModel.adapterStories.showLoadingFlow.collectLatest {
						viewModel.showLoadingForStores.value = it
					}
				}
				launch {
					viewModel.adapterNicknames.showLoadingFlow.collectLatest {
						viewModel.showLoadingForNicknames.value = it
					}
				}
				launch {
					viewModel.adapterCategories.showLoadingFlow.collectLatest {
						viewModel.showLoadingForCategories.value = it
					}
				}
			}
		}

		viewModel.filterType.observe(viewLifecycleOwner) {
			when (it) {
				null, TypeSearchResult.ADVERTISEMENT -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						viewModel.adapterAdvertisements,
						false,
						1
					)
				}
				TypeSearchResult.STORE -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						viewModel.adapterStories,
						false,
						2
					)
				}
				TypeSearchResult.NICKNAME -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						viewModel.adapterNicknames,
						false,
						2
					)
				}
				TypeSearchResult.CATEGORY -> {
					binding.recyclerView.setupWithRVItemCommonListUsage(
						viewModel.adapterCategories,
						false,
						3
					)
				}
			}
		}
	}

}
