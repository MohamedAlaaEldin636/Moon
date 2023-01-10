package grand.app.moon.extensions

/**
 * @param dividerAction only executed between items not after last item and not before first item.
 */
inline fun <reified E> List<E>.forEachWithDivider(
	dividerAction: () -> Unit,
	itemAction: (E) -> Unit
) {
	for ((index, item) in withIndex()) {
		itemAction(item)

		if (index != lastIndex) {
			dividerAction()
		}
	}
}

fun <E> List<E>?.orIfNullOrEmpty(fallback: () -> List<E>): List<E> {
	return if (isNullOrEmpty()) fallback() else this
}
