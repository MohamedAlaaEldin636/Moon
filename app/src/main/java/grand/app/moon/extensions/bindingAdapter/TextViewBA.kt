package grand.app.moon.extensions.bindingAdapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import grand.app.moon.core.extenstions.pxToSp
import grand.app.moon.core.extenstions.spToPx
import grand.app.moon.extensions.orZero
import kotlin.math.roundToInt

fun AppCompatTextView.adjustInsideRV(
	text: CharSequence,
	maxTextSizeInSpUsedInAutoSizing: Float = context.pxToSp(
		TextViewCompat.getAutoSizeMaxTextSize(this).toFloat()
	),
	minTextSizeInSpUsedInAutoSizing: Float = context.pxToSp(
		TextViewCompat.getAutoSizeMinTextSize(this).toFloat()
	),
) {

	TextViewCompat.setAutoSizeTextTypeWithDefaults(
		this,
		TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE
	)

	setTextSize(TypedValue.COMPLEX_UNIT_SP, maxTextSizeInSpUsedInAutoSizing)

	this.text = text

	post {
		TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
			this,
			minTextSizeInSpUsedInAutoSizing.roundToInt(),
			maxTextSizeInSpUsedInAutoSizing.roundToInt(),
			TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
			TypedValue.COMPLEX_UNIT_SP
		)
	}
}

@BindingAdapter("textView_setTextColorResName")
fun TextView.setTextColorResName(resName: String?) {
	setTextColor(
		context?.resources?.getIdentifier(
			resName, "color", context?.packageName ?: return
		) ?: return
	)
}

@BindingAdapter("textView_setHeightForSingleLine")
fun TextView.setHeightForSingleLine(heightInSp: Int) {
	val height = context.spToPx(heightInSp.toFloat())

	layoutParams = ViewGroup.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT,
		height.roundToInt()
	)
}

@BindingAdapter("textView_setTextColorResBA")
fun TextView.setTextColorResBA(@ColorRes res: Int?) {
	setTextColor(if (res == null) Color.TRANSPARENT else ContextCompat.getColor(context, res))
}

@BindingAdapter(
	"textView_serDrawableCompatBA_drawableStart",
	"textView_serDrawableCompatBA_drawableTop",
	"textView_serDrawableCompatBA_drawableEnd",
	"textView_serDrawableCompatBA_drawableBottom",
	requireAll = false
)
fun TextView.serDrawableCompatBA(
	start: Drawable?,
	top: Drawable?,
	end: Drawable?,
	bottom: Drawable?,
) {
	setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
}

@BindingAdapter("textView_makeItHorizontallyScrollable")
fun TextView.makeItHorizontallyScrollable(andFade: Boolean?) {
	setSingleLine()
	ellipsize = TextUtils.TruncateAt.MARQUEE
	marqueeRepeatLimit = -1
	isFocusable = true
	isFocusableInTouchMode = true
	canScrollHorizontally(-1)
	setHorizontallyScrolling(true)
	if (andFade == true) {
		isSelected = true
	}else {
		isHorizontalFadingEdgeEnabled = true
	}

	/*
	android:singleLine="true"
                android:ellipsize="marquee"
                android:marqueeRepeatLimit ="marquee_forever"
                android:focusable="true"
                android:focusableInTouchMode="true"
                android:scrollHorizontally="true"
	 */
}

/*
//        textView_serDrawableCompatBA_drawableResNameStart="@{`ic_right_add_adv`}"
@BindingAdapter(
	"textView_serDrawableCompatBA_drawableResNameStart",
	"textView_serDrawableCompatBA_drawableResNameTop",
	"textView_serDrawableCompatBA_drawableResNameEnd",
	"textView_serDrawableCompatBA_drawableResNameBottom",
	requireAll = false
)
fun TextView.serDrawableCompatBA(
	resNameStart: String?,
	resNameTop: String?,
	resNameEnd: String?,
	resNameBottom: String?,
) {
	setCompoundDrawablesRelativeWithIntrinsicBounds(
		context.getIdentifierDrawableOrZero(resNameStart),
		context.getIdentifierDrawableOrZero(resNameTop),
		context.getIdentifierDrawableOrZero(resNameEnd),
		context.getIdentifierDrawableOrZero(resNameBottom),
	)
}
 */
