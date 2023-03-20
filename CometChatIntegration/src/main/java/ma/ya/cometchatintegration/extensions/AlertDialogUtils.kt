package ma.ya.cometchatintegration.extensions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import ma.ya.cometchatintegration.R

fun FragmentActivity.showAlertDialog(
	title: String,
	message: String,
	onDismissListener: () -> Unit = {},
	onPositiveButtonClick: () -> Unit
) {
	AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(
			getString(R.string.ok)
		) { _, _ ->
			onPositiveButtonClick()
		}
		.setOnDismissListener {
			onDismissListener()
		}
		.create()
		.show()
}
