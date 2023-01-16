package grand.app.moon.extensions

import androidx.activity.result.ActivityResultLauncher

fun <I> ActivityResultLauncher<I>.launchSafely(input: I) {
	kotlin.runCatching {
		launch(input)
	}
}
