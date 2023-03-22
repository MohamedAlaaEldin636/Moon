package ma.ya.cometchatintegration.screens.viewModels

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ma.ya.cometchatintegration.extensions.*
import ma.ya.cometchatintegration.helperClasses.MATimer
import ma.ya.cometchatintegration.helperClasses.MyLogger
import ma.ya.cometchatintegration.screens.RecordingDialogFragment
import java.io.File
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class RecordingViewModel(application: Application) : AndroidViewModel(application) {

	private val filePath = "${app.externalCacheDir?.absolutePath.orEmpty()}/" +
		"audio_record_moon_${System.currentTimeMillis()}.3gp"

	val recorder = app.createMediaRecorderOrNull(filePath)

	private var player: MediaPlayer? = null

	val isInPlayState = MutableLiveData(false)

	val textOfTimeInPlayState = MutableLiveData("0:00")
	val textOfTimeInPauseState = MutableLiveData("0:00")

	val sliderPercentage = MutableLiveData(0f)

	val isPlayingSound = MutableLiveData(false)
	val isLoadingSound = MutableLiveData(false)

	private var allAreReleased = false

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
		val lengthInMs = (Uri.fromFile(File(filePath)).getLengthOfAudioInMs(app) ?: player?.duration?.toLong()).let {
			if (it == null || it == -1L) null else it
		}
		if (lengthInMs == null) sliderPercentage.postValue(50f) else {
			sliderPercentage.postValue(
				(currentTimeInMs.toDouble() / lengthInMs.toDouble() * 100.0).toFloat()
			)
		}
	}

	fun seekToInSound(percentage: Float) {
		val lengthInMs = (Uri.fromFile(File(filePath)).getLengthOfAudioInMs(app) ?: player?.duration?.toLong()).let {
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
				player?.seekTo(currentTimeInMs.toInt())
			}else {
				player = MediaPlayer().apply {
					setOnPreparedListener {
						isLoadingSound.value = false
						it.seekTo(currentTimeInMs.toInt())
						timerSound.start()
						timerSound.pause()
					}

					try {
						isLoadingSound.value = true
						setDataSource(filePath)
						prepare()
					} catch (e: IOException) {
						MyLogger.e("CometChatIntegration error in preparring 0 -> $e")
					}
				}
			}
		}
	}

	fun playOrResumeOrPauseSound() {
		isPlayingSound.value = isPlayingSound.value?.not()

		if (player == null) {
			player = MediaPlayer().apply {
				setOnPreparedListener {
					MyLogger.e("CometChatIntegration error on prepare listener 0")
					isLoadingSound.value = false
					it.start()
					timerSound.start()
					MyLogger.e("CometChatIntegration error on prepare listener 1")
				}

				try {
					isLoadingSound.value = true
					setDataSource(filePath)
					prepare()
					MyLogger.e("CometChatIntegration error in prepared successfully")
				} catch (e: IOException) {
					MyLogger.e("CometChatIntegration error in preparring 1 -> $e")
				}
			}
		}else if (isPlayingSound.value == true) {
			timerSound.resume()
			player?.start()
		}else {
			timerSound.pause()
			player?.pause()
		}
	}

	fun toggleState() {
		if (isInPlayState.value == true) {
			pause()
		}else {
			resume()
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
				timerRecording.stop()
				timerSound.stop()
				player?.stop()
				player?.release()
			}
		}
	}

	fun pause() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			recorder?.pause()
		}
		timerRecording.pause()

		isInPlayState.value = false
	}

	fun resume() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			recorder?.resume()
		}
		timerRecording.resume()

		isInPlayState.value = true
	}

	fun start() {
		recorder?.start()

		timerRecording.start()
		isInPlayState.value = true
	}

}
