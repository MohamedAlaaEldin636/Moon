package ma.ya.cometchatintegration.screens

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.slider.Slider
import ma.ya.cometchatintegration.R
import ma.ya.cometchatintegration.databinding.DialogFragmentRecordingBinding
import ma.ya.cometchatintegration.extensions.showError
import ma.ya.cometchatintegration.helperClasses.MADialogFragment
import ma.ya.cometchatintegration.helperClasses.MyLogger
import ma.ya.cometchatintegration.screens.viewModels.RecordingViewModel

class RecordingDialogFragment : MADialogFragment<DialogFragmentRecordingBinding>() {

	companion object {
		const val KEY_FRAGMENT_RESULT_FILE_PATH = "KEY_FRAGMENT_RESULT_FILE_PATH"
	}

	private var sliderIsBeingTouchedByUser = false

	private val viewModel by viewModels<RecordingViewModel>()

	override fun getLayoutId(): Int = R.layout.dialog_fragment_recording

	override val windowGravity: Int = Gravity.BOTTOM

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		MyLogger.e("sadhiasudh launched")
	}

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.dr_rect_20_top))
		window.setWindowAnimations(R.style.DialogBottomSheetAnim)
	}

	override fun initializeBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		if (viewModel.recorder == null) {
			context?.showError(getString(R.string.something_went_wrong_please_try_again))

			dismiss()

			return
		}

		binding.recordOrPauseFrameLayout.isVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

		binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
			override fun onStartTrackingTouch(slider: Slider) {
				// Responds to when slider's touch event is being started
				sliderIsBeingTouchedByUser = true
			}

			override fun onStopTrackingTouch(slider: Slider) {
				// Responds to when slider's touch event is being stopped
				sliderIsBeingTouchedByUser = false
			}
		})
		binding.slider.addOnChangeListener { _, value, fromUser ->
			// Responds to when slider's value is changed
			if (fromUser) {
				viewModel.seekToInSound(value)
			}//else Handled below isa.
		}

		viewModel.sliderPercentage.observe(viewLifecycleOwner) {
			val value = it ?: 0f

			if (sliderIsBeingTouchedByUser.not()) {
				binding.slider.value = value
			}
		}

		binding.root.post {
			viewModel.start()
		}
	}

	override fun onEitherCancelOrDismiss() {
		viewModel.releaseAllResources()
	}

}
