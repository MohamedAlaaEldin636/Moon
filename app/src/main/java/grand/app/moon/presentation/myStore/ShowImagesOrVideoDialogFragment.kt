package grand.app.moon.presentation.myStore

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.DialogFragmentAnnouncementBinding
import grand.app.moon.databinding.DialogFragmentShowImagesOrVideoBinding
import grand.app.moon.presentation.base.MADialogFragment
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
		binding.sliderView.setSliderAdapter(viewModel.adapter)

		binding.sliderView.post {
			binding.sliderView.setSliderAdapter(viewModel.adapter)
			binding.sliderView.startAutoCycle()
		}
	}

	override fun onStart() {
		super.onStart()

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			initializePlayer()
		}
	}

	override fun onResume() {
		super.onResume()

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || player == null) {
			initializePlayer()
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

	private fun initializePlayer() {
		if (viewModel.args.areImagesNotVideo.not()) {
			viewModel.files.firstOrNull()?.also { videoUrl ->
				player = SimpleExoPlayer.Builder(requireContext())
					.build()

				player?.also { exoPlayer ->
					val mediaItem = MediaItem.fromUri(videoUrl)
					exoPlayer.setMediaItem(mediaItem)

					exoPlayer.addListener(this)

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
