package grand.app.moon.extensions.bindingAdapter

import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.DrawableRes
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import grand.app.moon.extensions.orZero

@BindingAdapter("view_clipBackground")
fun View.clipBackground(clip: Boolean?) {
	outlineProvider = ViewOutlineProvider.BACKGROUND
	clipToOutline = true
	if (this is ViewGroup) {
		clipChildren = true
	}
}

@BindingAdapter("view_setBackgroundRes")
fun View.setBackgroundRes(@DrawableRes res: Int?) {
	setBackgroundResource(res.orZero())
}

@BindingAdapter("view_setBackgroundResName")
fun View.setBackgroundResName(resName: String?) {
	setBackgroundResource(
		context?.resources?.getIdentifier(
			resName, "drawable", context?.packageName ?: return
		) ?: return
	)
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
