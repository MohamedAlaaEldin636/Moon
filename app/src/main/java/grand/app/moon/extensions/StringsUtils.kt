package grand.app.moon.extensions

fun String?.trimAllWhitespaces(): String {
	return this?.filterNot { it.isWhitespace() }.orEmpty()
}
