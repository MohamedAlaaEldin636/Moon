package grand.app.moon.extensions.bindingAdapter

import android.content.res.ColorStateList
import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.orZero
import kotlin.math.roundToInt

@BindingAdapter("view_setBackgroundTintRes")
fun View.setBackgroundTintRes(@ColorRes res: Int?) {
	backgroundTintList = ColorStateList.valueOf(
		ContextCompat.getColor(context ?: return, res ?: return)
	)
}

@BindingAdapter("view_setBackgroundTintColorInt")
fun View.setBackgroundTintColorInt(@ColorInt color: Int?) {
	backgroundTintList = ColorStateList.valueOf(color ?: return)
}

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

@BindingAdapter(
	"view_layoutConstraintHeightMaxByPercentage",
	"view_layoutConstraintHeightMaxByPercentage_refView",
	requireAll = true
)
fun View.layoutConstraintHeightMaxByPercentage(
	@FloatRange(from = 0.0, to = 100.0) valueHundred: Float?,
	refView: View?
) {
	if (refView == null || valueHundred == null) return

	val value = valueHundred / 100f

	if (refView.height > 0) {
		val layoutParams = layoutParams as? ConstraintLayout.LayoutParams ?: return
		MyLogger.e("layoutConstraintHeightMaxByPercentage -> 0 -> $value ${refView.height} ${layoutParams.matchConstraintMaxHeight}")
		layoutParams.matchConstraintMaxHeight = (refView.height.toFloat() * value).roundToInt()
		MyLogger.e("layoutConstraintHeightMaxByPercentage -> 1 -> $value ${refView.height} ${layoutParams.matchConstraintMaxHeight}")
		this.layoutParams = layoutParams
	}else {
		refView.post {
			kotlin.runCatching {
				val layoutParams = layoutParams as? ConstraintLayout.LayoutParams ?: return@runCatching
				MyLogger.e("layoutConstraintHeightMaxByPercentage -> 000000 ${refView.height} ${layoutParams.matchConstraintMaxHeight}")
				layoutParams.matchConstraintMaxHeight = (refView.height.toFloat() * value).roundToInt()
				MyLogger.e("layoutConstraintHeightMaxByPercentage -> 11111 ${refView.height} ${layoutParams.matchConstraintMaxHeight}")
				this.layoutParams = layoutParams
			}
		}
	}
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
