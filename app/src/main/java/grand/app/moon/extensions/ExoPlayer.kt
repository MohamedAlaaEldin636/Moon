package grand.app.moon.extensions

import android.content.Context
import androidx.databinding.BindingAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun ExoPlayer.awaitReady() = suspendCoroutine { continuation ->
	val listener = object : Player.Listener {
		override fun onPlaybackStateChanged(playbackState: Int) {
			if (playbackState == Player.STATE_READY) {
				removeListener(this)
				//continuation.invokeOnCancellation { removeListener(listener) }
				continuation.resume(Unit)
			}
		}
	}
	addListener(listener)
	//continuation.invokeOnCancellation { removeListener(listener) }
}

@BindingAdapter(
	"exoPlayer_changeVideoLinkOrPause_exoPlayer",
	"exoPlayer_changeVideoLinkOrPause_value",
	"exoPlayer_changeVideoLinkOrPause_pause",
	requireAll = true
)
fun PlayerView.changeVideoLinkOrPause(exoPlayer: ExoPlayer?, value: String?, pause: Boolean?) {
	if (exoPlayer != null) {
		if (player == null) {
			player = exoPlayer
		}

		if (pause?.not().orFalse()) {
			exoPlayer.changeVideoLink(value.orEmpty())
		}else {
			exoPlayer.pause()
		}
	}
}

fun ExoPlayer.changeVideoLink(newVideoLink: String) {
	pause()

	if (newVideoLink.isEmpty()) return

	val mediaItem = MediaItem.fromUri(newVideoLink)
	setMediaItem(mediaItem)

	prepare()
}

fun Context.createExoPlayer(
	videoLink: String? = null,
	mute: Boolean = false,
	onBufferingVideo: ExoPlayer.() -> Unit = {},
	onFinishedPayingVideo: ExoPlayer.() -> Unit = { seekTo(0L) }
): ExoPlayer {
	return ExoPlayer.Builder(this).build().also { exoPlayer ->
		if (!videoLink.isNullOrEmpty()) {
			val mediaItem = MediaItem.fromUri(videoLink)
			exoPlayer.setMediaItem(mediaItem)
		}

		exoPlayer.addListener(object : Player.Listener {
			override fun onPlaybackStateChanged(playbackState: Int) {
				when (playbackState) {
					ExoPlayer.STATE_ENDED -> {
						exoPlayer.onFinishedPayingVideo()
					}
					ExoPlayer.STATE_BUFFERING -> {
						exoPlayer.onBufferingVideo()
					}
				}
			}
		})

		if (mute) {
			exoPlayer.volume = 0f
		}
		exoPlayer.playWhenReady = true

		if (!videoLink.isNullOrEmpty()) {
			exoPlayer.prepare()
		}
	}
}
