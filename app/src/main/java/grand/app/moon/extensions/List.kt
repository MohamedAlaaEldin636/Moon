package grand.app.moon.extensions

fun <T> MutableList<T>.swap(firstPosition: Int, secondPosition: Int) {
	if (firstPosition == secondPosition) return

	val tmp = this[firstPosition]
	this[firstPosition] = this[secondPosition]
	this[secondPosition] = tmp
}

inline fun <reified T> List<T>.indexOfOrNull(element: T) = indexOf(element).let {
	if (it == -1) null else it
}

inline fun <reified T> List<T>.toArrayList(): ArrayList<T> = arrayListOf(*this.toTypedArray())

inline fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
	return indexOfFirst(predicate).let { if (it == -1) null else it }
}

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
