package grand.app.moon.extensions

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
