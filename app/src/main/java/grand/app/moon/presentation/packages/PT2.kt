package grand.app.moon.presentation.packages

import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.geometry.Offset
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import kotlin.math.abs
import kotlin.math.roundToInt

private const val MIN_SCALE = 0.75f

class DepthPageTransformer2(
	private val offsetPx: Int,
	private val vertOffsetPx: Int,
	private val pageMarginPx: Int = 0,
	private val additionalOffsetPx: Float = 0f,
) : ViewPager2.PageTransformer {

	private fun requireViewPager(page: View): ViewPager2 {
		val parent = page.parent
		val parentParent = parent.parent
		if (parent is RecyclerView && parentParent is ViewPager2) {
			return parentParent
		}
		throw IllegalStateException(
			"Expected the page view to be managed by a ViewPager2 instance."
		)
	}

	override fun transformPage(view: View, position: Float) {
		view.apply {
			if (true) {
				val viewPager = requireViewPager(view)

				val constraintLayout = view.findViewById<ConstraintLayout>(R.id.constraintLayout)
				val card = view.findViewById<MaterialCardView>(R.id.backgroundView)
				when {
					position < -1 -> {
						translationZ = position
						//constraintLayout.elevation = position * 8f
						//card.cardElevation = position * 8f
						//card.maxCardElevation = position * 8f
					}
					position <= 0 -> {
						translationZ = position
						//constraintLayout.elevation = position * 8f
						//card.cardElevation = position * 8f
						//card.maxCardElevation = position * 8f
					}
					position <= 1 -> {
						translationZ = -position
						//constraintLayout.elevation = position * 8f
						//card.cardElevation = -position * 8f
						//card.maxCardElevation = -position * 8f
					}
					else -> {
						translationZ = -position
						//constraintLayout.elevation = position * 8f
						//card.cardElevation = -position * 8f
						//card.maxCardElevation = -position * 8f
					}
				}
				//constraintLayout.elevation = (position * context.dpToPx(8f)).coerceAtLeast(0f)

				// 65%
				val offset = position * -(2.5f * offsetPx + pageMarginPx)
				val totalMargin = offsetPx + pageMarginPx

				val marginTop = abs(vertOffsetPx * position)

				//val pageWidth = width
				//val pageWidth65Percent = pageWidth.toFloat() * 0.65f
				//val restOfPageWidth = pageWidth.toFloat() - pageWidth65Percent

				if (true || viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
					updateLayoutParams<ViewGroup.MarginLayoutParams> {
						marginStart = totalMargin
						marginEnd = totalMargin
						topMargin = marginTop.roundToInt()
					}

					alpha = 1f

					translationX = if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
						-offset + (position * additionalOffsetPx)
					} else {
						offset - (position * additionalOffsetPx)
					}
				} else {
					updateLayoutParams<ViewGroup.MarginLayoutParams> {
						topMargin = totalMargin
						bottomMargin = totalMargin
					}

					translationY = offset
				}

				return
			}







			val pageWidth = width
			when {
				position < -1 -> { // [-Infinity,-1)
					// This page is way off-screen to the left.
					alpha = 0f
				}
				position <= 0 -> { // [-1,0]
					// Use the default slide transition when moving to the left page
					alpha = 1f
					translationX = 0f
					translationZ = 0f
					scaleX = 0.5f
					scaleY = 0.5f

					// todo ...
				}
				position <= 1 -> { // (0,1]
					// Fade the page out.
					alpha = 1 - position

					// Counteract the default slide transition
					translationX = pageWidth * -position
					// Move it behind the left page
					translationZ = -1f

					// Scale the page down (between MIN_SCALE and 1)
					val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
					scaleX = scaleFactor
					scaleY = scaleFactor
				}
				else -> { // (1,+Infinity]
					// This page is way off-screen to the right.
					alpha = 1f
					translationX = (pageWidth * 2) * -position
					translationZ = -1f
					scaleX = 1f
					scaleY = 1f
				}
			}
		}
	}
}
