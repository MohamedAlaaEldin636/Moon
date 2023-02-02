package grand.app.moon.extensions

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

object MAConstraintLayout {
	object MALayoutParams
}

fun MAConstraintLayout.MALayoutParams.createWrapContentCenteredInParent(
	width: Int = ConstraintLayout.LayoutParams.WRAP_CONTENT,
	height: Int = ConstraintLayout.LayoutParams.WRAP_CONTENT
) = ConstraintLayout.LayoutParams(width, height).also {
	it.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
	it.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
	it.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
	it.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
}
fun MAConstraintLayout.MALayoutParams.createMatchConstraintsCenteredInParent(
	width: Int = 0,
	height: Int = 0
) = ConstraintLayout.LayoutParams(width, height).also {
	it.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
	it.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
	it.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
	it.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
}

fun ViewGroup.removeAllViewsExceptFirst() {
	if (childCount > 1) {
		for (index in childCount.dec() downTo 1) {
			removeViewAt(index)
		}
	}
}
