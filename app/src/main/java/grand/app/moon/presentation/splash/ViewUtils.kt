package grand.app.moon.presentation.splash

import android.view.View
import androidx.core.view.postDelayed

fun <V : View> V.postWithReceiverAndRunCatching(action: V.() -> Unit) {
	post {
		kotlin.runCatching {
			this.action()
		}
	}
}

fun <V : View> V.postDelayedWithReceiverAndRunCatching(delayInMs: Long, action: V.() -> Unit) {
	postDelayed(delayInMs) {
		kotlin.runCatching {
			this.action()
		}
	}
}
