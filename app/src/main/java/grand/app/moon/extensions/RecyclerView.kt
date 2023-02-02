package grand.app.moon.extensions

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun LayoutManager?.setInitialPrefetchItemCount(count: Int) {
	/*when (this) {
		is GridLayoutManager -> initialPrefetchItemCount = count
		is LinearLayoutManager -> initialPrefetchItemCount = count
	}*/
}

fun RecyclerView.Adapter<*>.notifyItemsChanged(vararg ids: Int?) {
	ids.filterNotNull().forEach {
		notifyItemChanged(it)
	}
}

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
