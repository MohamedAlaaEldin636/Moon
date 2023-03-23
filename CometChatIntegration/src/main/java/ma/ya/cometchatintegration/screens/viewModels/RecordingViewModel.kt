package ma.ya.cometchatintegration.screens.viewModels

import android.app.Application
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.DefaultDataSource
import ma.ya.cometchatintegration.extensions.*
import ma.ya.cometchatintegration.helperClasses.MATimer
import ma.ya.cometchatintegration.helperClasses.MyLogger
import ma.ya.cometchatintegration.screens.RecordingDialogFragment
import java.io.File
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class RecordingViewModel(application: Application) : AndroidViewModel(application) {

	//externalCacheDir
	private val filePath = "${app.externalCacheDir?.absolutePath.orEmpty()}/" +
		"audio_record_moon_${System.currentTimeMillis()}.3gp" // 3gp

	val recorder = app.createMediaRecorderOrNull(filePath)
	// todo if all are released in onCreate view then create it isa.

	private var player: ExoPlayer? = null

	private fun createExoPlayer(
		onReady: () -> Unit
	) {
		player = ExoPlayer.Builder(app).build().also { exoPlayer ->
			val filePath = "/storage/emulated/0/APKs/M/VID20230322154946.mp4"
			kotlin.runCatching {
				MyLogger.e("CometChatIntegration RRRRR $filePath")
				MyLogger.e("CometChatIntegration RRRRR ${File(filePath).exists()}")
				MyLogger.e("CometChatIntegration RRRRR ${File(filePath).canRead()}")
				MyLogger.e("CometChatIntegration RRRRR ${File(filePath).canWrite()}")
				MyLogger.e("CometChatIntegration RRRRR ${File(filePath).toUri()}")
				MyLogger.e("CometChatIntegration RRRRR $filePath")
				MyLogger.e("CometChatIntegration RRRRR ${Uri.parse(filePath)}")
				MyLogger.e("CometChatIntegration RRRRR ${Uri.fromFile(File(filePath))}")
			}
			val uri = Uri.parse(filePath)
			exoPlayer.setMediaItem(MediaItem.fromUri(uri))
			DefaultDataSource.Factory(app)

			exoPlayer.addListener(object : Player.Listener {
				override fun onPlaybackStateChanged(playbackState: Int) {
					MyLogger.e("CometChatIntegration state $playbackState, while ExoPlayer.STATE_READY -> ${ExoPlayer.STATE_READY}")
					when (playbackState) {
						ExoPlayer.STATE_READY -> {
							if (isLoadingSound.value == true) {
								onReady()
								isLoadingSound.value = false
							}
						}
						ExoPlayer.STATE_ENDED -> {
							isPlayingSound.value = false
							player?.pause()
							player?.seekTo(0L)
							sliderPercentage.value = 0f
							textOfTimeInPauseState.value = "0:00"
							timerSound.stopOrReset()
						}
					}
				}

				override fun onPlayerError(error: PlaybackException) {
					MyLogger.e("CometChatIntegration onPlayerError $error")
				}
			})

			isLoadingSound.value = true
			exoPlayer.playWhenReady = false
			exoPlayer.prepare()
		}
	}

	val isInPlayState = MutableLiveData(false)

	val textOfTimeInPlayState = MutableLiveData("0:00")
	val textOfTimeInPauseState = MutableLiveData("0:00")

	val sliderPercentage = MutableLiveData(0f)

	val isPlayingSound = MutableLiveData(false)
	val isLoadingSound = MutableLiveData(false)

	private var allAreReleased = false

	private var pausedRecording = false
	private var pausedPlaying = false

	private val timerRecording = MATimer(1_000, 1_000) { currentTimeInMs ->
		// Update text of recording sound
		val duration = currentTimeInMs.milliseconds
		val minutes = duration.inWholeMinutes
		val seconds = duration.inWholeSeconds - (minutes.minutes.inWholeSeconds)

		MyLogger.e("CometChatIntegration play state minutes $minutes seconds $seconds")

		textOfTimeInPlayState.postValue(
			"$minutes:${seconds.toStringOrEmpty().minLengthZerosPrefix(2)}"
		)
	}
	private val timerSound = MATimer(1_000, 1_000) { currentTimeInMs ->
		// Update text of recording sound
		val duration = currentTimeInMs.milliseconds
		val minutes = duration.inWholeMinutes
		val seconds = duration.inWholeSeconds - (minutes.minutes.inWholeSeconds)

		MyLogger.e("CometChatIntegration pause state minutes $minutes seconds $seconds")

		textOfTimeInPauseState.postValue(
			"$minutes:${seconds.toStringOrEmpty().minLengthZerosPrefix(2)}"
		)

		// Update slider as well isa.
		/*
		val file = File(context.externalCacheDir, "filename.jpg")
		val uri = FileProvider.getUriForFile(context, "com.example.myapp.fileprovider", file)

		player?.duration
		 */
		val filePath = "/storage/emulated/0/APKs/M/VID20230322154946.mp4"
		val lengthInMs = (Uri.parse(filePath).getLengthOfAudioInMs(app)/* ?: player?.duration*/).let {
			if (it == null || it == -1L) null else it
		}
		MyLogger.e("CometChatIntegration lengthInMs $lengthInMs")
		if (lengthInMs == null) sliderPercentage.postValue(50f) else {
			sliderPercentage.postValue(
				(currentTimeInMs.toDouble() / lengthInMs.toDouble() * 100.0).toFloat()
					.coerceAtMost(100f)
			)
		}
	}

	fun seekToInSound(percentage: Float) {
		val lengthInMs = (Uri.fromFile(File(filePath)).getLengthOfAudioInMs(app) ?: player?.duration).let {
			if (it == null || it == -1L) null else it
		}
		if (lengthInMs == null) sliderPercentage.value = 50f else {
			val currentTimeInMs = (lengthInMs.toDouble() * percentage.toDouble()).toLong()

			val duration = currentTimeInMs.milliseconds
			val minutes = duration.inWholeMinutes
			val seconds = duration.inWholeSeconds - (minutes.minutes.inWholeSeconds)

			MyLogger.e("CometChatIntegration pause state minutes $minutes seconds $seconds")

			textOfTimeInPauseState.value = "$minutes:${seconds.toStringOrEmpty().minLengthZerosPrefix(2)}"

			if (player != null) {
				player?.seekTo(currentTimeInMs)
			}else {
				createExoPlayer {
					player?.seekTo(currentTimeInMs)
					timerSound.startOrResume()
					timerSound.pause()
				}
			}
		}
	}

	fun playOrResumeOrPauseSound() {
		isPlayingSound.value = isPlayingSound.value?.not()

		if (player == null) {
			createExoPlayer {
				MyLogger.e("CometChatIntegration error on prepare listener 0")
				player?.play()
				timerSound.startOrResume()
				MyLogger.e("CometChatIntegration error on prepare listener 1")
			}
		}else if (isPlayingSound.value == true) {
			timerSound.startOrResume()
			player?.play()
		}else {
			timerSound.pause()
			player?.pause()
		}
	}

	fun toggleState() {
		MyLogger.e("aaaaaaaaaaa in toggle state")
		val performPause = isInPlayState.value == true
		isInPlayState.value = performPause.not()
		if (performPause) {
			pauseRecording()
		}else {
			resumeRecording()
		}
	}

	fun delete(view: View) {
		val fragment = view.findFragmentOrNull<RecordingDialogFragment>() ?: return

		releaseAllResources()

		kotlin.runCatching {
			val value = File(filePath).delete()
			MyLogger.e("CometChatIntegration deleted file $value")
		}.getOrElse {
			MyLogger.e("CometChatIntegration error deleting file $it")
		}

		fragment.dismiss()
	}

	fun send(view: View) {
		val fragment = view.findFragmentOrNull<RecordingDialogFragment>() ?: return

		releaseAllResources()

		fragment.setFragmentResultUsingJson(RecordingDialogFragment.KEY_FRAGMENT_RESULT_FILE_PATH, filePath)
		fragment.dismiss()
	}

	@Synchronized
	fun releaseAllResources() {
		if (allAreReleased.not()) {
			allAreReleased = true

			kotlin.runCatching {
				recorder?.stop()
				recorder?.release()
				timerRecording.stopOrReset()
				timerSound.stopOrReset()
				player?.stop()
				player?.release()
			}
		}
	}

	fun pauseRecording() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			recorder?.pause() // todo called before start or after stop
		}
		timerRecording.pause()
	}

	fun resumeRecording() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			recorder?.resume()
		}
		timerRecording.startOrResume()
	}

	fun startRecording() {
		recorder?.start()

		timerRecording.startOrResume()
		isInPlayState.value = true
	}

	fun resumeRecordingOrSoundAccToCurrentState() {
		if (pausedRecording) {
			pausedRecording = false
			pauseRecording()
		}else if (pausedPlaying) {
			pausedPlaying = false
			timerSound.startOrResume()
			player?.play()
		}
	}
	fun pauseRecordingOrSoundAccToCurrentState() {
		if (isInPlayState.value == true) {
			pausedRecording = true
			pauseRecording()
		}else if (isPlayingSound.value == true) {
			pausedPlaying = true
			timerSound.pause()
			player?.pause()
		}
	}

}
