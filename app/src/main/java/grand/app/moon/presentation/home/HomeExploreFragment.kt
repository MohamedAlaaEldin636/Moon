package grand.app.moon.presentation.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import coil.request.videoFramePercent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

/*
This is a pretty advanced use case :). We don't provide a way to synchronize SimpleExoPlayer (or ExoPlayer) instances. With a bit of work, however, you might manage to get all of the streams playing at once inside a single instance. You'll need to touch a few components, but at a high level you'll need to do something like:

Inject a custom RenderersFactory instance that builds as many video renderers as you need. You can probably extend DefaultRenderersFactory to do this, then override buildVideoRenderers to build N instances of MediaCodecVideoRenderer. You can see the implementation in DefaultRenderersFactory to see how it builds a MediaCodecVideoRenderer, and copy that.
If you have control over the source content, make it so that you have a single big manifest containing all of the adaptation sets that you need. You can then use DashMediaSource as normal. If this isn't possible, create all of the DashMediaSource instances and then merge them using MergingMediaSource.
At this point the remaining work is to ensure the TrackSelector selects the correct track(s) for each of the video renderers. DefaultTrackSelector wont do anything like this for you, so you'll probably need to implement your own TrackSelector from scratch.
Once that's all wired up, it might "just work", although note that many devices only have a limited number of video decoders available (and for low end devices, the limit might be 1).

https://github.com/google/ExoPlayer/issues/2855

maybe 1 player instance to do all the work isa.

for images to appear immediately u can usee the glide cache ability keep the BasePaging
source suspended till get all images as bitmap using intoBitmap used in a map screen before
which is loaded via glide and just leave it don't use the bitmap it shouold be already loaded w
bs kda isa.
 */
@AndroidEntryPoint
class HomeExploreFragment : BaseFragment<FragmentHomeExploreBinding>() {

	private val viewModel by viewModels<HomeExploreViewModel>()

	private var job: Job? = null

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

		binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					loadVideo()
				}
			}
		})

		job = viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.adapter.loadStateFlow.collectLatest { state ->
					MyLogger.e("zzzzzzzzzz ch ${state.refresh} ${state.append}")
					if (state.refresh is LoadState.NotLoading) {
						job?.cancel()
						job = null

						binding.recyclerView.postDelayed(500) {
							loadVideo()
						}
					}
				}
			}
		}
	}

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

	private fun loadVideo() {
		val context = context ?: return
		val start = binding.recyclerView.layoutManager.findFirstVisibleItemPosition() ?: return
		val end = binding.recyclerView.layoutManager.findLastVisibleItemPosition() ?: return

		val completeStart = binding.recyclerView.layoutManager.findFirstCompletelyVisibleItemPosition() ?: return
		val completeEnd = binding.recyclerView.layoutManager.findLastCompletelyVisibleItemPosition() ?: return

		var canBePlayedViewHolder: VHPagingItemCommonListUsageWithExoPlayer<*, *>? = null
		var canBePlayedItem: ItemHomeExplore? = null
		var isPlayingVideo = false
		for (index in start..end) {
			val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(index)
			val item = viewModel.adapter.snapshot().items.getOrNull(index) ?: return
			if (viewHolder is VHPagingItemCommonListUsageWithExoPlayer<*, *>) {
				val binding = viewHolder.binding as? ItemHomeExploreBinding

				val isCompletelyVisible = index in completeStart..completeEnd

				if (canBePlayedViewHolder == null && item.isVideo && isCompletelyVisible) {
					canBePlayedViewHolder = viewHolder
					canBePlayedItem = item
				}

				if (viewHolder.player?.isPlaying == true || viewHolder.player?.isLoading == true) {
					if (isCompletelyVisible) {
						isPlayingVideo = true
						break
					}else {
						binding?.playerView?.isVisible = false
						viewHolder.releasePlayer()
					}
				}
			}
		}

		val binding = canBePlayedViewHolder?.binding
		if (isPlayingVideo.not() && binding is ItemHomeExploreBinding) {
			binding.playerView.isVisible = true
			binding.playerView.player = canBePlayedViewHolder?.playVideo(
				context, canBePlayedItem?.files?.firstOrNull().orEmpty()
			)
		}
	}

}
