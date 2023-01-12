package grand.app.moon.extensions

import java.math.RoundingMode

fun Int?.orZero() = this ?: 0
fun Float?.orZero() = this ?: 0f
fun Long?.orZero() = this ?: 0L
fun Double?.orZero() = this ?: 0.0

fun Boolean?.orFalse() = this ?: false

fun Float.round(decimalsCount: Int): Float =
	toBigDecimal().setScale(decimalsCount, RoundingMode.HALF_UP).toFloat()

fun Float.toIntIfNoFractionsOrThisFloat(): Number = if (this - toInt().toFloat() == 0f) toInt() else this

fun Any?.toStringOrEmpty(): String = this?.toString().orEmpty()
