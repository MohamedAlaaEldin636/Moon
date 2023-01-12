package grand.app.moon.extensions

import android.text.Spannable
import android.text.Spanned

operator fun Spannable.set(substring: String, span: Any) {
	val startIndex = indexOf(substring)

	if (startIndex == -1) return

	val endIndex = startIndex + substring.length

	setSpan(span, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
}
