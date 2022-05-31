package grand.app.moon.presentation.base.extensions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import grand.app.moon.presentation.base.utils.Constants

fun <A : Activity> Activity.openActivityAndClearStack(activity: Class<A>) {
  Intent(this, activity).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    intent = intent
    startActivity(this)
    finishAffinity()
  }
}


fun Activity.checkGalleryPermissions(): Boolean {
  val array = arrayOf(
    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
  )
  val deniedPermissions = ArrayList<String>()

  for (permission in array)
    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
      deniedPermissions.add(permission)

  if (deniedPermissions.isNotEmpty()) {
    val permissions = arrayOfNulls<String>(deniedPermissions.size)
    for (index in 0 until deniedPermissions.size) {
      permissions[index] = deniedPermissions[index]
    }
    ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_GALLERY)
  }
  return deniedPermissions.isEmpty()
}

fun <A : Activity> Activity.openActivity(activity: Class<A>) {
  Intent(this, activity).apply {
    startActivity(this)
  }
}