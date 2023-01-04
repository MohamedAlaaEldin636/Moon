package grand.app.moon.extensions.bindingAdapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import grand.app.moon.extensions.orZero

@BindingAdapter("view_setBackgroundRes")
fun View.setBackgroundRes(@DrawableRes res: Int?) {
	setBackgroundResource(res.orZero())
}

@BindingAdapter("view_enableAndAllChildren", "view_enableAndAllChildren_disableAlpha")
fun View.enableAndAllChildren(enable: Boolean?, disableAlpha: Float?) {
	val value = enable ?: false

	isEnabled = value
	alpha = if (value) 1f else disableAlpha ?: 0.5f

	if (this is ViewGroup) {
		children.forEach {
			it.enableAndAllChildren(value, disableAlpha)
		}
	}
}
