package grand.app.moon.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError

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

fun Context.showAlertDialog(
    title: String,
    message: String,
    positiveText: CharSequence,
    negativeText: CharSequence,
    isCancellable: Boolean = true,
    onPositiveButtonClick: (DialogInterface) -> Unit,
    onNegativeButtonClick: (DialogInterface) -> Unit,
    onDismissListener: () -> Unit = {},
) {
    kotlin.runCatching {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancellable)
            .setPositiveButton(positiveText) { dialog, _ ->
                onPositiveButtonClick(dialog)
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                onNegativeButtonClick(dialog)
            }
            .setOnDismissListener {
                onDismissListener()
            }
            .create()
            .show()
    }
}

fun <I> ActivityResultLauncher<I>.launchSafely(context: Context?, input: I) {
    try {
        launch(input)
    } catch (exception: ActivityNotFoundException) {
        context?.showError(context.getString(R.string.no_app_can_handle_this_action))
    }
}
