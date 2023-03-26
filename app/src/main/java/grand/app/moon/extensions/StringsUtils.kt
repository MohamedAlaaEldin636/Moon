package grand.app.moon.extensions

fun String?.trimAllWhitespaces(): String {
	return this?.filterNot { it.isWhitespace() }.orEmpty()
}

fun String?.trimFirstPlusIfExistsOrEmpty(): String {
	return orEmpty().let { if (it[0] == '+' && it.length > 1) it.substring(1) else it }
}
