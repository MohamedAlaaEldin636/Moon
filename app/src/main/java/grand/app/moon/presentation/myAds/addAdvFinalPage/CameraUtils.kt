package grand.app.moon.presentation.myAds.addAdvFinalPage

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import grand.app.moon.helpers.utils.getUriFromBitmapRetrievedByCamera
import grand.app.moon.helpers.utils.handleCaptureImageRotation
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {

	var imageUri: Uri? = null
		private set
	private var fileCameraCapture: File? = null
	var tag: Int? = null

	fun createImageUri(context: Context): Uri? {
		fileCameraCapture = File(
			context.applicationContext.filesDir,
			"camera_photo_${System.currentTimeMillis()}.png"
		)

		imageUri = context.applicationContext.let {
			FileProvider.getUriForFile(
				it,
				"grand.app.moon.fileprovider",
				fileCameraCapture ?: return@let null
			)
		}

		return imageUri
	}

	private fun Fragment.abc() {
		// todo ....
		val bitmap = handleCaptureImageRotation(fileCameraCapture, imageUri)
		bitmap?.let {
			imageUri = getUriFromBitmapRetrievedByCamera(it)
			//viewModel.request.uri = imageUri
			//loadImageProfile()
			//navigateSafe(ProfileFragmentDirections.actionProfileFragmentToCropFragment(viewModel.request))
		}
	}

}
