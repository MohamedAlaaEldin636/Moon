package grand.app.moon.extensions

inline fun <reified N : Number> N?.orZero(): N {
	return (when (this) {
		is Int -> 0
		is Float -> 0f
		is Long -> 0L
		is Double -> 0.0
		else -> throw RuntimeException("Unexpected type of Numebr")
	}) as N
}

