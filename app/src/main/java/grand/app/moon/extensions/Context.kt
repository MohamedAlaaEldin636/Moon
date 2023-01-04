package grand.app.moon.extensions

import android.content.Context

fun Context?.getIdentifierDrawableOrZero(name: String?): Int {
	return this?.resources?.getIdentifier(name.orEmpty(), "drawable", packageName).orZero()
}
