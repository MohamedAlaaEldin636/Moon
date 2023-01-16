package grand.app.moon.extensions

import androidx.recyclerview.widget.RecyclerView

inline fun <reified ID : RecyclerView.ItemDecoration> RecyclerView.addUniqueTypeItemDecoration(
	itemDecoration: ID
) {
	var toBeRemovedIndex: Int? = null
	for (index in 0 until itemDecorationCount) {
		if (getItemDecorationAt(index) is ID) {
			toBeRemovedIndex = index

			break
		}
	}

	if (toBeRemovedIndex != null) {
		removeItemDecorationAt(toBeRemovedIndex)
	}
	addItemDecoration(itemDecoration)
}
