package grand.app.moon.presentation.splash

import android.view.View
import androidx.core.view.postDelayed
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <V : View> V.postWithReceiverAndRunCatchingUntilSpecificCriteria(
	criteria: V.() -> Boolean,
	action: V.() -> Unit,
) {
	while (true) {
		postWithReceiverAndRunCatchingSuspended {
			if (criteria()) {
				action()
			}
		}
	}
}

suspend fun <V : View> V.postWithReceiverAndRunCatchingSuspended(action: V.() -> Unit) {
	suspendCoroutine {
		post {
			kotlin.runCatching {
				it.resume(this.action())
			}
		}
	}
}

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
