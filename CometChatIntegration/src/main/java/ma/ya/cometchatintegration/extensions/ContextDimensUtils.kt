package ma.ya.cometchatintegration.extensions

import android.content.Context
import android.util.TypedValue

fun Context.dpToPx(value: Float): Float {
	return TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
	)
}
