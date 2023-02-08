package grand.app.moon.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.get

inline fun <reified P : ViewGroup> View.firstParentWithId(@IdRes id: Int): P? {
	var parent = parent
	while (true) {
		if (parent is P && parent.id == id) {
			return parent
		}

		parent = (parent as? View)?.parent ?: break
	}

	return null
}

inline fun <reified P : ViewGroup> View.firstParentInstance(): P? {
	var parent = parent
	while (true) {
		if (parent is P) {
			return parent
		}

		parent = (parent as? View)?.parent ?: break
	}

	return null
}

fun ViewGroup.getOrNull(index: Int) = kotlin.runCatching { get(index) }.getOrNull()
