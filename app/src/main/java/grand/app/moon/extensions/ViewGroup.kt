package grand.app.moon.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.children
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
inline fun <reified P : View> ViewGroup.firstChildInstance(): P? {
	val parents = mutableListOf(this)
	while (parents.isNotEmpty()) {
		val parent = parents.getOrNull(0)
		parents.removeAt(0)
		if (parent != null) {
			for (view in parent.children) {
				if (view is P) {
					return view
				}else if (view is ViewGroup) {
					parents.add(view)
				}
			}
		}
	}

	return null
}

fun ViewGroup.getOrNull(index: Int) = kotlin.runCatching { get(index) }.getOrNull()
