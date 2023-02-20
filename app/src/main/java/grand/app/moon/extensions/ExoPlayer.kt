package grand.app.moon.extensions

import android.content.Context
import androidx.databinding.BindingAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun <T> Continuation<T>.resumeSafely(value: T) {
	kotlin.runCatching { resume(value) }
}

fun ExoPlayer.setPlayWhenReadyIfNotSet() {
	if (playWhenReady.not()) {
		play()
	}
}

suspend fun ExoPlayer.awaitPlaying() = suspendCoroutine { continuation ->
	val listener = object : Player.Listener {
		override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
			if (playbackSuppressionReason == Player.PLAYBACK_SUPPRESSION_REASON_NONE && playbackState == Player.STATE_READY) {
				setPlayWhenReadyIfNotSet()

				removeListener(this)

				continuation.resumeSafely(Unit)
			}
		}

		override fun onPlaybackStateChanged(playbackState: Int) {
			if (playbackState == Player.STATE_READY && playbackSuppressionReason == Player.PLAYBACK_SUPPRESSION_REASON_NONE) {
				setPlayWhenReadyIfNotSet()

				removeListener(this)

				continuation.resumeSafely(Unit)
			}
		}
	}

	setPlayWhenReadyIfNotSet()
	if (isPlaying) {
		continuation.resume(Unit)
	}else {
		addListener(listener)
	}
}

suspend fun ExoPlayer.awaitReady() = suspendCoroutine { continuation ->
	val listener = object : Player.Listener {
		override fun onPlaybackStateChanged(playbackState: Int) {
			if (playbackState == Player.STATE_READY) {
				removeListener(this)

				continuation.resume(Unit)
			}
		}
	}

	if (playbackState == Player.STATE_READY) {
		continuation.resume(Unit)
	}else {
		addListener(listener)
	}
}

@BindingAdapter("exoPlayer_setPlayerBA")
fun PlayerView.setPlayerBA(exoPlayer: ExoPlayer?) {
	player = exoPlayer
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

fun ExoPlayer.changeVideoLink(newVideoLink: String, seekToIfSameLink: Long? = null) {
	val currentLink = currentMediaItem?.localConfiguration?.uri?.toString().orEmpty()

	if (currentLink == newVideoLink) {
		if (seekToIfSameLink != null) {
			seekTo(seekToIfSameLink)
		}

		return
	}

	stop()

	if (newVideoLink.isEmpty()) return

	val mediaItem = MediaItem.fromUri(newVideoLink)
	setMediaItem(mediaItem)

	prepare()
}

fun Context.createExoPlayer(
	videoLink: String? = null,
	mute: Boolean = false,
	onBufferingVideo: (ExoPlayer.() -> Unit)? = null /*{}*/,
	onNotReadyVideo: (ExoPlayer.() -> Unit)? = null /*{}*/,
	onFinishedPayingVideo: (ExoPlayer.() -> Unit)? = null /*{ seekTo(0L) }*/
): ExoPlayer {
	return ExoPlayer.Builder(this).build().also { exoPlayer ->
		if (!videoLink.isNullOrEmpty()) {
			val mediaItem = MediaItem.fromUri(videoLink)
			exoPlayer.setMediaItem(mediaItem)
		}

		if (onBufferingVideo != null || onNotReadyVideo != null || onFinishedPayingVideo != null) {
			exoPlayer.addListener(object : Player.Listener {
				override fun onPlaybackStateChanged(playbackState: Int) {
					if (playbackState != ExoPlayer.STATE_READY) {
						onNotReadyVideo?.invoke(exoPlayer)
					}

					when (playbackState) {
						ExoPlayer.STATE_ENDED -> {
							onFinishedPayingVideo?.invoke(exoPlayer)
						}
						ExoPlayer.STATE_BUFFERING -> {
							onBufferingVideo?.invoke(exoPlayer)
						}
					}
				}
			})
		}

		if (mute) {
			exoPlayer.volume = 0f
		}

		if (!videoLink.isNullOrEmpty()) {
			exoPlayer.playWhenReady = true

			exoPlayer.prepare()
		}
	}
}
