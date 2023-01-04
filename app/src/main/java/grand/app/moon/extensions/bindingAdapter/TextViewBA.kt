package grand.app.moon.extensions.bindingAdapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import grand.app.moon.extensions.orZero

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
