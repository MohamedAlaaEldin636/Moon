package ma.ya.cometchatintegration.extensions

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ma.ya.cometchatintegration.R
import ma.ya.cometchatintegration.databinding.DialogFragmentRecordingBinding
import ma.ya.cometchatintegration.helperClasses.MyLogger

class DialogRecording(
	shownCustomDialog: ShownCustomDialog<DialogFragmentRecordingBinding>,
	private val recorder: MediaRecorder,
	private val filePath: String,
	private val onSuccess: (filePath: String) -> Unit,
) {

	companion object {
		fun setupDialog(binding: DialogFragmentRecordingBinding) {
			binding.recordOrPauseFrameLayout.isVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
		}
	}

	private val dialog = shownCustomDialog.dialog
	private val binding = shownCustomDialog.binding

	init {
		// Clicks & listeners Setups
		binding.deleteImageView.setOnClickListener {
			stopRecording()

			dialog.dismiss()
		}

		binding.sendImageView.setOnClickListener {
			stopRecording()

			onSuccess(filePath)
			dialog.dismiss()
		}

		/*slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
			override fun onStartTrackingTouch(slider: Slider) {
				// Responds to when slider's touch event is being started
			}

			override fun onStopTrackingTouch(slider: Slider) {
				// Responds to when slider's touch event is being stopped
			}
		})

		slider.addOnChangeListener { slider, value, fromUser ->
			// Responds to when slider's value is changed
		}*/
	}

	private fun stopRecording() {
		recorder.stop()
		recorder.release()
	}

	fun pause() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			recorder.pause()
		}

		adjustUIToPausedState()
	}

	fun resume() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			recorder.resume()
		}

		adjustUIToPlayingState()
	}

	fun start() {
		recorder.start()

		adjustUIToPlayingState()
	}

	private fun adjustUIToPlayingState() {
		binding.timeInPlayStateTextView.show()
		binding.timeInPauseStateTextView.makeInvisible()
		binding.slider.makeInvisible()
		binding.playOrPauseInPauseStateImageView.makeInvisible()

		binding.recordOrPauseStateImageView.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
	}
	private fun adjustUIToPausedState() {
		binding.timeInPlayStateTextView.makeInvisible()
		binding.timeInPauseStateTextView.show()
		binding.slider.show()
		binding.playOrPauseInPauseStateImageView.show()

		binding.recordOrPauseStateImageView.setImageResource(R.drawable.ic_baseline_keyboard_voice_24)
	}

}

// todo should pause when in background isa.
fun Fragment.showRecordingDialog(
	onSuccess: (filePath: String) -> Unit,
): DialogRecording? {
	val context = context

	val fileName = "${context?.externalCacheDir?.absolutePath.orEmpty()}/" +
		"audio_record_moon_${System.currentTimeMillis()}.3gp"

	val recorder = context?.createMediaRecorderOrNull(fileName)

	if (context == null || recorder == null) {
		MyLogger.e("context $context recorder null 1 or 2 returns null")

		context?.showError(context.getString(R.string.something_went_wrong_please_try_again))

		return null
	}

	val shownCustomDialog = showCustomDialog<DialogFragmentRecordingBinding>(
		activity,
		R.layout.dialog_fragment_recording
	) {
		DialogRecording.setupDialog(this)
	}

	if (shownCustomDialog == null) {
		MyLogger.e("creation of dialog returns null")

		context.showError(context.getString(R.string.something_went_wrong_please_try_again))

		return null
	}

	val dialogRecording = DialogRecording(/*this, context, */shownCustomDialog, recorder, fileName, onSuccess)

	shownCustomDialog.binding.root.post {
		dialogRecording.start()
	}

	return dialogRecording
}
