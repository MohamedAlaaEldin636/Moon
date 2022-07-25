package grand.app.moon.helpers.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.io.File
import java.util.*

fun Fragment.getBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
  var rotatedBitmap: Bitmap? = null
  when (orientation) {
    ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap =
      TransformationUtils.rotateImage(bitmap, 90)
    ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap =
      TransformationUtils.rotateImage(bitmap, 180)
    ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap =
      TransformationUtils.rotateImage(bitmap, 270)
    ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
    else -> rotatedBitmap = bitmap
  }
  return rotatedBitmap
}

fun Fragment.handleCaptureImageRotation(fileCameraCapture: File?,imageUri: Uri?): Bitmap? {
  var orientation: Int? = null
  var bitmap: Bitmap? = null
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    val ei = ExifInterface(fileCameraCapture!!.absoluteFile)
    bitmap = BitmapFactory.decodeFile(fileCameraCapture!!.path)
    orientation = ei.getAttributeInt(
      ExifInterface.TAG_ORIENTATION,
      ExifInterface.ORIENTATION_UNDEFINED
    )
    return getBitmap(bitmap, orientation)
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    context?.contentResolver?.openInputStream(imageUri!!).use { inputStream ->
      inputStream?.let {
        val exif = ExifInterface(it)
        orientation = exif.getAttributeInt(
          ExifInterface.TAG_ORIENTATION,
          ExifInterface.ORIENTATION_NORMAL
        )
        bitmap = BitmapFactory.decodeFile(fileCameraCapture?.path)
        orientation?.let { itOrientation ->
          return bitmap?.let { getBitmap(it, itOrientation) }
        }
      }
    }
  }
  return null
}

fun Fragment.getUriFromBitmapRetrievedByCamera(bitmap: Bitmap): Uri {
  val bitmapScaled = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
  val path = MediaStore.Images.Media.insertImage(
    requireContext().contentResolver,
    bitmapScaled,
    Date(System.currentTimeMillis()).toString() + "photo",
    null
  )
  return Uri.parse(path)
}



