package grand.app.moon.extensions

import android.app.Dialog

fun Dialog.showSafely() {
	kotlin.runCatching { show() }.getOrElse {
		MyLogger.e("Dialog.show Safely -> $it")
	}
}

fun Dialog.dismissSafely() {
	kotlin.runCatching { dismiss() }.getOrElse {
		MyLogger.e("Dialog.dismiss Safely -> $it")
	}
}
