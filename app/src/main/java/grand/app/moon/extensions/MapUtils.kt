package grand.app.moon.extensions

fun <K, V> Map<K, V>.toHashMap() = HashMap(this)
