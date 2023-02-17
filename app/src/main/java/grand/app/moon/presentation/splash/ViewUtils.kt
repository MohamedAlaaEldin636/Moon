package grand.app.moon.presentation.splash

import android.view.View

fun <V : View> V.postWithReceiverAndRunCatching(action: V.() -> Unit) {
	post {
		kotlin.runCatching {
			this.action()
		}
	}
}
