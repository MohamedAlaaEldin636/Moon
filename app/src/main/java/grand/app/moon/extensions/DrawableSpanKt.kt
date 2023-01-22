package grand.app.moon.extensions

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ReplacementSpan
import kotlin.math.abs

class DrawableSpanKt(
	private val drawable: Drawable,
	private val useDrawableHeightIfPossible: Boolean = false,
	private val makeHeightSameAsWidthIfPossible: Boolean = false,
) : ReplacementSpan() {

	private val width by lazy {
		drawable.intrinsicWidth
	}

	override fun getSize(
		paint: Paint,
		text: CharSequence?,
		start: Int,
		end: Int,
		fm: Paint.FontMetricsInt?
	): Int {
		return width
	}

	override fun draw(
		canvas: Canvas,
		text: CharSequence?,
		start: Int,
		end: Int,
		x: Float,
		top: Int,
		y: Int,
		bottom: Int,
		paint: Paint
	) {
		val xInt = x.toInt()
		if (useDrawableHeightIfPossible && abs(top - bottom) > drawable.intrinsicHeight) {
			val height = drawable.intrinsicHeight
			val half = height / 2
			drawable.setBounds(xInt, top + half, xInt + width, bottom - half)
		}else if (makeHeightSameAsWidthIfPossible && abs(top - bottom) > width) {
			val halfWidth = width / 2
			drawable.setBounds(xInt, top + halfWidth, xInt + width, bottom - halfWidth)
		}else {
			drawable.setBounds(xInt, top, xInt + width, bottom)
		}
		drawable.draw(canvas)
	}

}
