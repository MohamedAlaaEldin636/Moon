package grand.app.moon.extensions

fun Int?.orZero() = this ?: 0
fun Float?.orZero() = this ?: 0f
fun Long?.orZero() = this ?: 0L
fun Double?.orZero() = this ?: 0.0

fun Boolean?.orFalse() = this ?: false
