package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentHomeExploreBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.HomeExploreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeExploreFragment : BaseFragment<FragmentHomeExploreBinding>() {

	private val viewModel by viewModels<HomeExploreViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_home_explore

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.layoutManager = requireContext().getExploreLayoutManager()
		binding.recyclerView.adapter = viewModel.adapter.withDefaultHeaderAndFooterAdapters()

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.explores.collectLatest {
					viewModel.adapter.submitData(it)
				}
			}
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			viewModel.adapter.refresh()
		}
	}

}
