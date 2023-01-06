package grand.app.moon.extensions

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import grand.app.moon.R

fun Context?.getIdentifierDrawableOrZero(name: String?): Int {
	return this?.resources?.getIdentifier(name.orEmpty(), "drawable", packageName).orZero()
}

fun Context.getAsRequiredText(text: CharSequence) = buildSpannedString {
	append("$text *")

	this[length.dec()..length] = ForegroundColorSpan(R.color.required_3)
}
