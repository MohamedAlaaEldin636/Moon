package grand.app.moon.extensions

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * - Line only between items, Not after each item which will lead to divider after end of last item
 * which isn't good in the design as they are means to be dividers only not boundaries as well isa.
 *
 * - Dividers are always assumed for [LinearLayoutManager.VERTICAL], If need a general case it's
 * easy to alter the computations below, but we only provided this case as it's the only case
 * in the app here isa.
 *
 * @param margin means right & left margins only again this is due to this specific case else it
 * should provide more customization & clarification isa.
 */
class MADividerItemDecoration(
	@ColorInt dividerColor: Int = Color.TRANSPARENT,
	@Px private val horizontalSpacing: Int = 0,
	@Px private val verticalSpacing: Int = 0,
) : RecyclerView.ItemDecoration() {
	
	private val drawable = dividerColor.toDrawable()
	
	private val bounds = Rect()

	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		val position = parent.getChildAdapterPosition(view).run { if (this == RecyclerView.NO_POSITION) 0 else this }
		val itemCount = parent.adapter?.itemCount ?: 0
		val layoutManager = parent.layoutManager
		when {
			layoutManager is GridLayoutManager && layoutManager.spanCount > 1 -> {
				TODO("Not Programmed yet grid layout manager with span count > 1")
			}
			(layoutManager is GridLayoutManager && layoutManager.spanCount == 1) || layoutManager is LinearLayoutManager -> when {
				itemCount == 1 -> {
					outRect.set(horizontalSpacing, verticalSpacing, horizontalSpacing, verticalSpacing)
				}
				((layoutManager as? GridLayoutManager)?.orientation == GridLayoutManager.VERTICAL) ||
					((layoutManager as? LinearLayoutManager)?.orientation == LinearLayoutManager.VERTICAL) -> when (position) {
					0 -> {
						// Top edge
						outRect.set(horizontalSpacing, verticalSpacing, horizontalSpacing, verticalSpacing / 2)
					}
					itemCount.dec() -> {
						// Bottom edge
						outRect.set(horizontalSpacing, verticalSpacing / 2, horizontalSpacing, verticalSpacing)
					}
					else -> {
						// No edges
						outRect.set(horizontalSpacing, verticalSpacing / 2, horizontalSpacing, verticalSpacing / 2)
					}
				}
				else -> when (position) {
					/*0 -> {
						// Start edge
						outRect.set(horizontalSpacing, verticalSpacing, horizontalSpacing / 2, verticalSpacing)
					}
					itemCount.dec() -> {
						// Bottom edge
						outRect.set(horizontalSpacing / 2, verticalSpacing, horizontalSpacing, verticalSpacing)
					}*/
					else -> {
						// No edges
						outRect.set(horizontalSpacing / 2, verticalSpacing, horizontalSpacing / 2, verticalSpacing)
					}
				}
			}
			else -> {
				TODO("Not Programmed yet unknown layout manager")
			}
		}
	}
	
	override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		if (parent.layoutManager == null) {
			return
		}

		val isVerticalOrientation = (parent.layoutManager as? LinearLayoutManager)?.orientation == LinearLayoutManager.VERTICAL ||
			(parent.layoutManager as? GridLayoutManager)?.orientation == GridLayoutManager.VERTICAL

		if (isVerticalOrientation) {
			drawVertical(canvas, parent)
		}else {
			drawHorizontal(canvas, parent)
		}
	}

	private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
		canvas.save()
		val top: Int
		val bottom: Int
		//noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
		if (parent.clipToPadding) {
			top = parent.paddingTop
			bottom = parent.height - parent.paddingBottom
			canvas.clipRect(
				parent.paddingLeft, top,
				parent.width - parent.paddingRight, bottom
			)
		} else {
			top = 0
			bottom = parent.height
		}

		val childCount: Int = parent.childCount
		for (i in 0 until childCount) {
			val child: View = parent.getChildAt(i)
			parent.layoutManager?.getDecoratedBoundsWithMargins(child, bounds)
			val right: Int = bounds.right + child.translationX.roundToInt()
			val left: Int = right - horizontalSpacing//(if (i == 0) horizontalSpacing / 2 else horizontalSpacing)
			drawable.setBounds(left, top, right, bottom)
			drawable.draw(canvas)
		}
		canvas.restore()
	}
	private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
		canvas.save()
		val left: Int
		val right: Int
		if (parent.clipToPadding) {
			left = parent.paddingLeft
			right = parent.width - parent.paddingRight
			canvas.clipRect(
				left, parent.paddingTop, right,
				parent.height - parent.paddingBottom
			)
		} else {
			left = 0
			right = parent.width
		}
		val childCount = parent.childCount
		for (i in 0 until childCount) {
			val child = parent.getChildAt(i)
			parent.layoutManager?.getDecoratedBoundsWithMargins(child, bounds) // or just parent.
			val bottom: Int = bounds.bottom + child.translationY.roundToInt()
			val top: Int = bottom - verticalSpacing
			drawable.setBounds(left, top, right, bottom)
			drawable.draw(canvas)
		}
		canvas.restore()
	}

	private fun drawVertical3(canvas: Canvas, parent: RecyclerView) {
		canvas.save()
		val top: Int
		val bottom: Int
		//noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
		if (parent.clipToPadding) {
			top = parent.paddingTop
			bottom = parent.height - parent.paddingBottom
			canvas.clipRect(
				parent.paddingLeft, top,
				parent.width - parent.paddingRight, bottom
			)
		} else {
			top = 0
			bottom = parent.height
		}

		val childCount: Int = parent.childCount
		for (i in 0 until childCount) {
			val child: View = parent.getChildAt(i)
			parent.layoutManager?.getDecoratedBoundsWithMargins(child, bounds)
			val right: Int = bounds.right + child.translationX.roundToInt()
			val left: Int = right - horizontalSpacing//(if (i == 0) horizontalSpacing / 2 else horizontalSpacing)
			drawable.setBounds(left, top, right, bottom)
			drawable.draw(canvas)
		}
		canvas.restore()
	}

}
