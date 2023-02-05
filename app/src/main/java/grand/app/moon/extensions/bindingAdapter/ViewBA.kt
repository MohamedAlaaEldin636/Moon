package grand.app.moon.extensions.bindingAdapter

import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.orZero
import kotlin.math.roundToInt

@BindingAdapter("view_translateXByWholeWidthPercent")
fun View.translateXByWholeWidthPercent(@FloatRange(from = 0.0, to = 1.0) percentage: Float?) {
	if (percentage == null) return

	post {
		translationX = width.toFloat() * percentage
	}
}

/*@BindingAdapter("view_constraintPercentHeightToAnotherView")
fun View.constraintPercentHeightToAnotherView(otherView: View?, @FloatRange(from = 0.0, to = 1.0) percentage: Float) {
	otherView?.post {
		val height = otherView.height

		val params = layoutParams
		params.height = (height.toFloat() * percentage).roundToInt()
		layoutParams = params
	}
}*/

@BindingAdapter("view_constraintPercentWidth")
fun View.constraintPercentWidth(@FloatRange(from = 0.0, to = 1.0) value: Float?) {
	val layoutParams = layoutParams as? ConstraintLayout.LayoutParams ?: return
	layoutParams.matchConstraintPercentWidth = value.orZero()
		.coerceAtLeast(0f).coerceAtMost(1f)
	this.layoutParams = layoutParams
}

@BindingAdapter("view_constraintPercentHeight")
fun View.constraintPercentHeight(@FloatRange(from = 0.0, to = 1.0) value: Float?) {
	val layoutParams = layoutParams as? ConstraintLayout.LayoutParams ?: return
	layoutParams.matchConstraintPercentHeight = value.orZero()
		.coerceAtLeast(0f).coerceAtMost(1f)
	this.layoutParams = layoutParams
}

@BindingAdapter("view_layoutConstraintHeightMaxByPercentage")
fun View.layoutConstraintHeightMaxByPercentage(@FloatRange(from = 0.0, to = 1.0) value: Float?) {
	val layoutParams = layoutParams as? ConstraintLayout.LayoutParams ?: return
	MyLogger.e("layoutConstraintHeightMaxByPercentage -> 0 -> ${layoutParams.matchConstraintDefaultHeight} ${layoutParams.matchConstraintMaxHeight} $value")
	layoutParams.matchConstraintMaxHeight = (layoutParams.matchConstraintDefaultHeight.toFloat() * (value ?: return)).roundToInt()
	MyLogger.e("layoutConstraintHeightMaxByPercentage -> 1 -> ${layoutParams.matchConstraintDefaultHeight} ${layoutParams.matchConstraintMaxHeight} $value")
	this.layoutParams = layoutParams
}

@BindingAdapter("view_layoutConstraintHorizontalBias")
fun View.layoutConstraintHorizontalBias(@FloatRange(from = 0.0, to = 1.0) value: Float?) {
	val layoutParams = layoutParams as? ConstraintLayout.LayoutParams ?: return
	layoutParams.horizontalBias = value.orZero()
		.coerceAtLeast(0f).coerceAtMost(1f)
	this.layoutParams = layoutParams
}

@BindingAdapter("view_visibleOrInvisible")
fun View.visibleOrInvisible(show: Boolean?) {
	visibility = if (show == true) View.VISIBLE else View.INVISIBLE
}

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
