package grand.app.moon.presentation.home

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.insertHeaderItem
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentHomeExploreSubsectionBinding
import grand.app.moon.databinding.ItemHomeExploreBinding
import grand.app.moon.databinding.ItemHomeExploreSubsectionBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.models.ItemHomeExplore
import grand.app.moon.presentation.home.viewModels.HomeExploreSubsectionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeExploreSubsectionFragment : BaseFragment<FragmentHomeExploreSubsectionBinding>() {

	private val viewModel by viewModels<HomeExploreSubsectionViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_home_explore_subsection

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter.withDefaultHeaderAndFooterAdapters(),
			false,
			1
		)

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.explores.collectLatest {
					MyLogger.e("djasdakskdash 3 -> ${viewModel.initialData.map { it.files }}")
					viewModel.adapter.submitData(it.insertHeaderItems(viewModel.initialData))
				}
			}
		}

		binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					loadVideo()
				}
			}
		})

		observeAdapterOnDataFirstLoadedOnceHaveDataUsingViewLifecycle(
			viewModel.adapter
		) {
			if (viewModel.player == null) {
				binding.recyclerView.post {
					loadVideo()
				}
			}
		}
	}

	private fun loadVideo() {
		val context = context ?: return

		val start = binding.recyclerView.layoutManager.findFirstVisibleItemPosition() ?: return
		val end = binding.recyclerView.layoutManager.findLastVisibleItemPosition() ?: return
		val completeStart = binding.recyclerView.layoutManager.findFirstCompletelyVisibleItemPosition()
		val completeEnd = binding.recyclerView.layoutManager.findLastCompletelyVisibleItemPosition()

		var pickedIndex: Int? = null
		for (index in start..end) {
			if (viewModel.adapter.snapshot().items.getOrNull(index)?.isVideo.orFalse()) {
				pickedIndex = index

				break
			}
		}
		var pickedCompleteIndex: Int? = null
		if (completeStart != null && completeEnd != null) {
			if (completeStart == completeEnd) {
				pickedCompleteIndex = completeStart
			}else {
				for (index in completeStart..completeEnd) {
					if (viewModel.adapter.snapshot().items.getOrNull(index)?.isVideo.orFalse()) {
						pickedCompleteIndex = index

						break
					}
				}
			}
		}

		val index = pickedCompleteIndex ?: pickedIndex ?: return viewModel.releasePlayer()
		val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(index)
		val item = viewModel.adapter.snapshot().items.getOrNull(index) ?: return viewModel.releasePlayer()
		val visibleItemView = binding.recyclerView.layoutManager?.findViewByPosition(index) ?: return viewModel.releasePlayer()
		val visibleHeight = Rect().let {
			visibleItemView.getGlobalVisibleRect(it)

			it.height()
		}

		if ((visibleHeight.toFloat() / binding.recyclerView.height.toFloat()) < 0.5f) {
			return viewModel.releasePlayer()
		}

		if (item.isVideo && viewHolder is VHPagingItemCommonListUsage<*, *>) {
			val binding = viewHolder.binding

			if (binding is ItemHomeExploreSubsectionBinding) {
				binding.playerView.player = viewModel.playVideo(context, item.files?.firstOrNull().orEmpty())
			}
		}
	}

	override fun onStart() {
		super.onStart()
	}

	override fun onResume() {
		super.onResume()

		kotlin.runCatching { viewModel.resumePlayer() }
	}

	override fun onPause() {
		kotlin.runCatching { viewModel.pausePlayer() }

		super.onPause()
	}

	override fun onStop() {
		super.onStop()
	}

}