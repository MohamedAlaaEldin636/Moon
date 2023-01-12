package grand.app.moon.extensions

import android.graphics.Canvas
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
/*class MADividerItemDecoration(
	@ColorInt dividerColor: Int,
	@Px private val horizontalSpacing: Int,
	@Px private val verticalSpacing: Int,
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

		when (layoutManager) {
			is GridLayoutManager -> {
				layoutManager.orientation
			}
			is LinearLayoutManager -> when {
				itemCount == 1 -> {
					outRect.set(horizontalSpacing, verticalSpacing, horizontalSpacing, verticalSpacing)
				}
				layoutManager.orientation == LinearLayoutManager.VERTICAL -> when {
					position == 0 -> {
						// Top edge
						outRect.set(horizontalSpacing, verticalSpacing, horizontalSpacing, verticalSpacing / 2)
					}
					position == itemCount.dec() -> {
						// Bottom edge
						outRect.set(horizontalSpacing, verticalSpacing / 2, horizontalSpacing, verticalSpacing)
					}
					else -> {
						// No edges
						outRect.set(horizontalSpacing, verticalSpacing / 2, horizontalSpacing, verticalSpacing / 2)
					}
				}
				else -> {

				}
			}
			else -> {
				General.TODO("Program it later.")

				return
			}
		}
		
		if (layoutManager !is LinearLayoutManager
			|| layoutManager.orientation != LinearLayoutManager.VERTICAL
			|| itemCount < 2
			|| position == itemCount.dec()) {
			return
		}
		
		outRect.set(0, 0, 0, dividerThickness)
	}
	
	override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		canvas.save()
		
		val left: Int
		val right: Int
		if (parent.clipToPadding) {
			left = parent.paddingLeft + margin
			right = parent.width - parent.paddingRight - margin
			canvas.clipRect(
				left, parent.paddingTop, right,
				parent.height - parent.paddingBottom
			)
		} else {
			left = 0 + margin
			right = parent.width - margin
		}
		
		for (i in 0 until parent.childCount.dec()) {
			val child = parent.getChildAt(i)
			parent.getDecoratedBoundsWithMargins(child, bounds)
			val bottom: Int = bounds.bottom + child.translationY.roundToInt()
			val top: Int = bottom - dividerThickness
			drawable.setBounds(left, top, right, bottom)
			drawable.draw(canvas)
		}
		
		canvas.restore()
	}
	
}*/
