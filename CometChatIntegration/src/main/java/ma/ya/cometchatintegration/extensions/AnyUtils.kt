package ma.ya.cometchatintegration.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object AnyUtils {

	@JvmStatic
	fun print(any: Any?) = toString()

}

fun Any?.shouldShowRequestPermissionRationale(permission: String): Boolean = when (this) {
	is Fragment -> shouldShowRequestPermissionRationale(permission)
	is AppCompatActivity -> ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
	else -> false
}

fun Any?.getActivityOrNull() = when (this) {
	is Fragment -> activity
	is AppCompatActivity -> this
	else -> null
}

fun <I, O> Any?.registerForActivityResult(
	contract: ActivityResultContract<I, O>,
	callback: ActivityResultCallback<O>
): ActivityResultLauncher<I> {
	return when (this) {
		is Fragment -> {
			registerForActivityResult(contract, callback)
		}
		is AppCompatActivity -> {
			registerForActivityResult(contract, callback)
		}
		else -> throw RuntimeException("Unexpected host $this")
	}
}
