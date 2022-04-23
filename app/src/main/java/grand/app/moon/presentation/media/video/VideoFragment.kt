package grand.app.moon.presentation.media.video

import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentVideoBinding
import grand.app.moon.presentation.base.BaseFragment

@AndroidEntryPoint
class VideoFragment : BaseFragment<FragmentVideoBinding>() {
  var player: SimpleExoPlayer? = null

  val args : VideoFragmentArgs by navArgs()
  override
  fun getLayoutId() = R.layout.fragment_video

  override
  fun setBindingVariables() {
    initializePlayer()
  }

  private val MIN_BUFFER_DURATION = 2000

  //Max Video you want to buffer during PlayBack
  private val MAX_BUFFER_DURATION = 5000

  //Min Video you want to buffer before start Playing it
  private val MIN_PLAYBACK_START_BUFFER = 1500

  //Min video You want to buffer when user resumes video
  private val MIN_PLAYBACK_RESUME_BUFFER = 2000

  private fun initializePlayer() {
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
      val mediaDataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
        requireContext(),
        Util.getUserAgent(requireContext(), "mediaPlayerSample")
      )
      val mediaSource: ProgressiveMediaSource =
        ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
          MediaItem.fromUri(args.video)
        )
      val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)
      player = SimpleExoPlayer.Builder(requireContext())
        .setMediaSourceFactory(mediaSourceFactory)
        .setLoadControl(loadControl)
        .build()
      player!!.addMediaSource(mediaSource)
      player!!.prepare()
      binding.videoFullScreenPlayer.hideController()
      binding.videoFullScreenPlayer.useController = true
      binding.videoFullScreenPlayer.player = player
      player!!.play()
      player!!.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
          if (state == ExoPlayer.STATE_BUFFERING) {
//                        binding.spinnerVideoDetails.setVisibility(View.VISIBLE);
          } else {
            binding.spinnerVideoDetails.visibility = View.GONE
          }
        }
      })
    }
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onPause() {
    if (player != null) {
      player!!.stop()
      player!!.release()
      player!!.playWhenReady = false
    }
    player = null
    super.onPause()
  }

}