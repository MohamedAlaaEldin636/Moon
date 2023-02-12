package grand.app.moon.extensions

import androidx.databinding.ViewDataBinding


inline fun <reified T> ViewDataBinding.getUsingRootViaJson() =
	(root.tag as? String).fromJsonInlinedOrNull<T>()

inline fun <reified T> ViewDataBinding.setUsingRootViaJson(value: T) {
	root.tag = value.toJsonInlinedOrNull()
}

