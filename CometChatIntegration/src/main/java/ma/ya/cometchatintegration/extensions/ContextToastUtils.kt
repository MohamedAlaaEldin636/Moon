package ma.ya.cometchatintegration.extensions

import android.content.Context
import android.widget.Toast

private var toast: Toast? = null

fun Context.showError(
	msg: CharSequence,
	duration: Int = Toast.LENGTH_SHORT,
	modifications: (Toast) -> Unit = {}
) = showToast(msg, duration, modifications)

private fun Context.showToast(msg: CharSequence, duration: Int, modifications: (Toast) -> Unit) {
	toast = Toast.makeText(this, msg, duration).also(modifications)
}
