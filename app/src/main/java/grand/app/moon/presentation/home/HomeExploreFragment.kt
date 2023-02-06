package grand.app.moon.presentation.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentHomeExploreBinding
import grand.app.moon.databinding.ItemHomeExploreBinding
import grand.app.moon.databinding.ItemHomeRvBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.models.ItemHomeExplore
import grand.app.moon.presentation.home.viewModels.HomeExploreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeExploreFragment : BaseFragment<FragmentHomeExploreBinding>() {

	private val viewModel by viewModels<HomeExploreViewModel>()

	//private val listener: ((CombinedLoadStates) -> Unit)? = null

	// todo if still lagging use global loading and view above rv then scroll rv a little down then a little back up garabha kda isa.

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

		/*binding.recyclerView.clearOnScrollListeners()
		binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					MyLogger.e("hello zzzzzzzzzzzzzz -2")
					setupRvs()
				}
			}
		})

		val listener: (CombinedLoadStates) -> Unit = {
			if (viewModel.adapter.snapshot().items.isNotEmpty()) {
				MyLogger.e("hello zzzzzzzzzzzzzz -1")
				setupRvs()
			}
			listener?.also {
				viewModel.adapter.removeLoadStateListener(listener)
			}
		}
		viewModel.adapter.addLoadStateListener(listener)*/

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			viewModel.adapter.refresh()
		}
	}

	/*private fun setupRvs() {
		binding.recyclerView.post {
			MyLogger.e("hello zzzzzzzzzzzzzz 0")

			val start = binding.recyclerView.layoutManager.findFirstVisibleItemPosition() ?: return@post
			val end = binding.recyclerView.layoutManager.findLastVisibleItemPosition() ?: return@post

			for (index in start..end) {
				val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(index)
				MyLogger.e("hello zzzzzzzzzzzzzz 1")
				if (viewHolder is VHPagingItemCommonListUsageWithExoPlayer<*, *>) {
					MyLogger.e("hello zzzzzzzzzzzzzz 2")
					@Suppress("UNCHECKED_CAST")
					val holder = viewHolder as? VHPagingItemCommonListUsageWithExoPlayer<ItemHomeExploreBinding, ItemHomeExplore>
						?: continue
					MyLogger.e("hello zzzzzzzzzzzzzz 3")

					val binding = holder.getBindingOrNull() ?: continue

					viewModel.setupRvs(binding, viewModel.adapter.snapshot().items[index], index, holder)
				}
			}
		}
	}
*/
	override fun onPause() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
			kotlin.runCatching { viewModel.adapter.releaseAllPlayers(binding.recyclerView) }
		}

		super.onPause()
	}

	override fun onStop() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			kotlin.runCatching { viewModel.adapter.releaseAllPlayers(binding.recyclerView) }
		}

		super.onStop()
	}

	override fun onDestroyView() {
		lifecycleScope.launch {
			viewModel.adapter.submitData(PagingData.empty())
		}

		super.onDestroyView()
	}

}
