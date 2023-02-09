package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.FragmentSearchResultsBinding
import grand.app.moon.databinding.FragmentSearchSuggestionsBinding
import grand.app.moon.extensions.MADividerItemDecoration
import grand.app.moon.extensions.addUniqueTypeItemDecoration
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.models.TypeSearchResult
import grand.app.moon.presentation.home.viewModels.SearchResultsViewModel
import grand.app.moon.presentation.home.viewModels.SearchSuggestionsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
			}
		}

		viewModel.filterType.observe(viewLifecycleOwner) {
			binding.recyclerView.adapter = when (it) {
				null, TypeSearchResult.ADVERTISEMENT -> viewModel.adapterAdvertisements
				TypeSearchResult.STORE -> viewModel.adapterStories
				TypeSearchResult.NICKNAME -> viewModel.adapterNicknames
				TypeSearchResult.CATEGORY -> viewModel.adapterCategories
			}
		}
	}

}
