package ma.ya.cometchatintegration.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import ma.ya.cometchatintegration.R

fun <I> ActivityResultLauncher<I>.launchSafely(context: Context?, input: I) {
	try {
		launch(input)
	} catch (exception: ActivityNotFoundException) {
		context?.showError(context.getString(R.string.no_app_can_handle_this_action))
	}
}
