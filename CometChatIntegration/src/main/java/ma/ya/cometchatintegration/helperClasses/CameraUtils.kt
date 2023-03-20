package ma.ya.cometchatintegration.helperClasses

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object CameraUtils {

	var imageUri: Uri? = null
		private set
	private var fileCameraCapture: File? = null
	var tag: Int? = null

	var selectMultipleImages = false

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

}
