package grand.app.moon.extensions

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import grand.app.moon.core.extenstions.dpToPx
import kotlin.math.roundToInt

fun Context.getExploreLayoutManager(): LayoutManager {
	val smallItemHeight = dpToPx(102f).roundToInt()

	return object : StaggeredGridLayoutManager(
		3,
		VERTICAL,
	) {
		override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams?): Boolean {
			val position = kotlin.runCatching { layoutParams?.bindingAdapterPosition }.getOrNull()

			if (layoutParams != null && position != null) {
				//val position = layoutParams.absoluteAdapterPosition

				// 0, 9, 10, 19, 20, 29, 30, 39, 40, has height double any other item isa.

				// 0, 7, 10, 17, 20
				if (position == 0 || position % 10 == 0 || position.plus(3) % 10 == 0) {
					layoutParams.height = smallItemHeight * 2
				}else {
					layoutParams.height = smallItemHeight
				}
			}

			//generateDefaultLayoutParams()
			// 102dp or 204dp
			//layoutParams?.height

			//layoutParams?.bindingAdapterPosition
			//layoutParams?.absoluteAdapterPosition
			//layoutParams?.viewLayoutPosition

			return super.checkLayoutParams(layoutParams)
		}
	}
}

/*
fun Context.getExploreLayoutManager(): LayoutManager {
	val smallItemHeight = dpToPx(102f).roundToInt()

	return object : StaggeredGridLayoutManager(
		3,
		VERTICAL
	) {
		override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams?): Boolean {
			val position = kotlin.runCatching { layoutParams?.bindingAdapterPosition }.getOrNull()

			if (layoutParams != null && position != null) {
				//val position = layoutParams.absoluteAdapterPosition

				// 0, 9, 10, 19, 20, 29, 30, 39, 40, has height double any other item isa.

				if (position == 0 || position % 10 == 0 || position.inc() % 10 == 0) {
					layoutParams.height = smallItemHeight * 2
				}else {
					layoutParams.height = smallItemHeight
				}
			}

			//generateDefaultLayoutParams()
			// 102dp or 204dp
			//layoutParams?.height

			//layoutParams?.bindingAdapterPosition
			//layoutParams?.absoluteAdapterPosition
			//layoutParams?.viewLayoutPosition

			return super.checkLayoutParams(layoutParams)
		}
	}
}

 */