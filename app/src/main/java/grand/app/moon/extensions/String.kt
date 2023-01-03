package grand.app.moon.extensions

fun String?.orElseIfNullOrEmpty(fallback: String): String {
	return if (isNullOrEmpty()) fallback else this
}
