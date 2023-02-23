package grand.app.moon.extensions

fun <T> T?.ifNotNull(action: (T) -> Unit) {
	if (this != null) {
		action(this)
	}
}

fun String?.ifNotNullNorEmpty(action: (String) -> Unit) {
	if (!isNullOrEmpty()) {
		action(this)
	}
}

fun String.ifNotEmpty(action: (String) -> Unit) {
	if (isNotEmpty()) {
		action(this)
	}
}

fun String?.orElseIfNullOrEmpty(fallback: String): String {
	return if (isNullOrEmpty()) fallback else this
}

fun String.minLengthZerosPrefix(requiredLength: Int): String {
	return if (length < requiredLength) {
		"${"0".repeat(requiredLength - length)}$this"
	}else {
		this
	}
}
