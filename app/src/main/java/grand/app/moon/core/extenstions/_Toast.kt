package grand.app.moon.core.extenstions

import android.content.Context
import android.widget.Toast
import grand.app.moon.core.MyApplication

fun Context.showSuccessToast(msg: String) {
	(applicationContext as? MyApplication)?.showSuccessToast(msg) ?: simpleToast(msg)
}

fun Context.showErrorToast(msg: String) {
	(applicationContext as? MyApplication)?.showErrorToast(msg) ?: simpleToast(msg)
}

fun Context.showNormalToast(msg: String) {
	(applicationContext as? MyApplication)?.showNormalToast(msg) ?: simpleToast(msg)
}

private fun Context.simpleToast(msg: String) {
	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
