package grand.app.moon.presentation.myStore

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.DialogFragmentAnnouncementBinding
import grand.app.moon.databinding.DialogFragmentShowImagesOrVideoBinding
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.viewModels.AnnouncementViewModel
import grand.app.moon.presentation.myStore.viewModel.ShowImagesOrVideoViewModel

@AndroidEntryPoint
class ShowImagesOrVideoDialogFragment : MADialogFragment<DialogFragmentShowImagesOrVideoBinding>(),
	Player.Listener {

	override fun getLayoutId(): Int = R.layout.dialog_fragment_show_images_or_video

	override val canceledOnTouchOutside: Boolean = false

	override val heightIsMatchParent: Boolean = true

	private val viewModel by viewModels<ShowImagesOrVideoViewModel>()

	private var player: ExoPlayer? = null

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(
			ColorDrawable(
				ContextCompat.getColor(requireContext(), R.color.announcement_dialog_window_background)
			)
		)
	}

	override fun initializeBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initializePlayer2()
		/*binding.playerView.hideController()
		binding.playerView.useController = true*/
		//binding.playerView.player = player

		binding.sliderView.setSliderAdapter(viewModel.adapter)

		binding.sliderView.post {
			binding.sliderView.setSliderAdapter(viewModel.adapter)
			binding.sliderView.startAutoCycle()

			binding.sliderView.post {
				viewModel.adapter.submitList(viewModel.files)
			}
		}
	}

	override fun onStart() {
		super.onStart()

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//initializePlayer()
		}
	}

	override fun onResume() {
		super.onResume()

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || player == null) {
			//initializePlayer()
		}
	}

	override fun onPause() {
		super.onPause()

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
			releasePlayer()
		}
	}

	override fun onStop() {
		super.onStop()

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			releasePlayer()
		}
	}

	private val MIN_BUFFER_DURATION = 2000

	//Max Video you want to buffer during PlayBack
	private val MAX_BUFFER_DURATION = 5000

	//Min Video you want to buffer before start Playing it
	private val MIN_PLAYBACK_START_BUFFER = 1500

	//Min video You want to buffer when user resumes video
	private val MIN_PLAYBACK_RESUME_BUFFER = 2000

	private fun initializePlayer2() {
		if (viewModel.args.areImagesNotVideo.not()) {
			if (player == null) {
				val allocator = DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE)
				val loadControl: LoadControl = DefaultLoadControl.Builder()
					.setAllocator(DefaultAllocator(true, 16))
					.setBufferDurationsMs(
						MIN_BUFFER_DURATION,
						MAX_BUFFER_DURATION,
						MIN_PLAYBACK_START_BUFFER,
						MIN_PLAYBACK_RESUME_BUFFER
					)
					.setTargetBufferBytes(-1)
					.setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl()
				val mediaDataSourceFactory: com.google.android.exoplayer2.upstream.DataSource.Factory = DefaultDataSourceFactory(
					requireContext(),
					Util.getUserAgent(requireContext(), "mediaPlayerSample")
				)
				val mediaSource: ProgressiveMediaSource =
					ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
						MediaItem.fromUri(viewModel.files.firstOrNull().orEmpty())
					)
				val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)
				player = SimpleExoPlayer.Builder(requireContext())
					.setMediaSourceFactory(mediaSourceFactory)
					.setLoadControl(loadControl)
					.build()
				player!!.addMediaSource(mediaSource)
				player!!.prepare()
				binding.playerView.hideController()
				binding.playerView.useController = true
				binding.playerView.player = player
				player!!.play()
				player!!.addListener(object : Player.Listener {
					override fun onPlaybackStateChanged(state: Int) {
						if (state == ExoPlayer.STATE_BUFFERING) {
//                        binding.spinnerVideoDetails.setVisibility(View.VISIBLE);
						} else {
							//binding.spinnerVideoDetails.visibility = View.GONE
						}
					}
				})
			}
		}
	}

	private fun initializePlayer() {
		if (viewModel.args.areImagesNotVideo.not()) {
			viewModel.files.firstOrNull()?.also { videoUrl ->
				player = /*Simple*/ExoPlayer.Builder(requireContext())
					//.setLoadControl()
					.build()

				/*if (_binding != null && _binding?.playerView?.player == null) {
					_binding?.playerView?.player = player
				}*/

				player?.also { exoPlayer ->
					val mediaItem = MediaItem.fromUri(videoUrl)
					exoPlayer.setMediaItem(mediaItem)

					//exoPlayer.addListener(this)

					exoPlayer.playWhenReady = true
					exoPlayer.prepare()
				}
			}
		}
	}

	/*override fun onPlaybackStateChanged(playbackState: Int) {
		if (playbackState == ExoPlayer.STATE_READY) {
			viewModel.showAudioLoading.value = false
		}else if (playbackState == ExoPlayer.STATE_ENDED) {
			viewModel.showAudioPlayNotPause.value = true
			playerSingle?.pause()
			playerSingle?.seekTo(0L)
		}
	}*/

	private fun releasePlayer() {
		player?.release()
		player = null
	}

	private fun showLoadingAndData() {
		//binding.innerConstraintLayout.visibility = View.INVISIBLE
		showLoading()
	}
	private fun hideLoadingAndData() {
		//binding.innerConstraintLayout.visibility = View.VISIBLE
		hideLoading()
	}

}
