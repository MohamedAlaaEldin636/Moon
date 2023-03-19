package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentFilterResults2Binding
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.helpers.paging.withDefaultFooterOnlyAdapter
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.FilterNavHomeGraphViewModel
import grand.app.moon.presentation.home.viewModels.FilterResults2ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterResults2Fragment : BaseFragment<FragmentFilterResults2Binding>() {

	private val viewModel by viewModels<FilterResults2ViewModel>()

	private val navGraphViewModel by navGraphViewModels<FilterNavHomeGraphViewModel>(R.id.nav_home)

	override fun getLayoutId(): Int = R.layout.fragment_filter_results_2

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter.withDefaultFooterOnlyAdapter(),
			false,
			2
		)

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.adapter.showLoadingFlow.collectLatest {
					viewModel.showWholePageLoader.value = it
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.repoShop.getFilterResults(navGraphViewModel.filter).collectLatest {
					viewModel.adapter.submitData(it)
				}
			}
		}
	}

}
