package grand.app.moon.presentation.packages

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.viewpager2.widget.ViewPager2
import grand.app.moon.core.extenstions.dpToPx
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

//private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

private const val MIN_SCALE = 0.75f

/*internal class CardSliderTransformer(private val viewPager: ViewPager2) :
	ViewPager2.PageTransformer {

	private val startOffset: Float

	init {
		val windowManager = viewPager.context
			.getSystemService(Context.WINDOW_SERVICE) as WindowManager

		val screen = Point()
		windowManager.defaultDisplay.getSize(screen)

		val horizontalPadding = viewPager.paddingEnd + viewPager.paddingStart
		startOffset = ((horizontalPadding / 2).toFloat() / (screen.x - horizontalPadding).toFloat())
	}

	override fun transformPage(page: View, position: Float) {

		if (position.isNaN())
			return

		val absPosition = (position - startOffset).absoluteValue

		if (absPosition >= 1) {
			ViewCompat.setElevation(page, viewPager.minShadow)
			page.alpha = viewPager.smallAlphaFactor

			if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL){
				page.scaleY = viewPager.smallScaleFactor
				page.scaleX = 1f
			} else {
				page.scaleY = 1f
				page.scaleX = viewPager.smallScaleFactor
			}

		} else {
			// This will be during transformation
			ViewCompat.setElevation(
				page, scalingEquation(viewPager.minShadow, viewPager.baseShadow, absPosition)
			)

			page.alpha = scalingEquation(viewPager.smallAlphaFactor, 1f, absPosition)

			if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL){
				page.scaleY = scalingEquation(viewPager.smallScaleFactor, 1f, absPosition)
				page.scaleX = 1f
			} else {
				page.scaleY = 1f
				page.scaleX = scalingEquation(viewPager.smallScaleFactor, 1f, absPosition)
			}
		}
	}


	private fun scalingEquation(minValue: Float, maxValue: Float, absPosition: Float) =
		(minValue - maxValue) * absPosition + maxValue

}*/

/*class SSS : ViewPager2.PageTransformer {
	override fun transformPage(page: View, offsetPosition: Float) {
		page.apply {
			if (offsetPosition === 0f) {
				val paddingLeft: Float = getPaddingLeft()
				val paddingRight: Float = this@ScaleViewPager.getPaddingRight()
				val width: Float = this@ScaleViewPager.getMeasuredWidth()
				offsetPosition = paddingLeft / (width - paddingLeft - paddingRight)
			}
			val currentPos: Float = position - offsetPosition
			if (itemWidth === 0) {
				itemWidth = view.getWidth()
				//由于左右边的缩小而减小的x的大小的一半
				reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f
			}
			if (currentPos <= -1.0f) {
				view.setTranslationX(reduceX + mCoverWidth)
				view.setScaleX(mScaleMin)
				view.setScaleY(mScaleMin)
			} else if (currentPos <= 1.0) {
				val scale: Float = (mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos))
				val translationX: Float = currentPos * -reduceX
				if (currentPos <= -0.5) { //两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
					view.setTranslationX(translationX + mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f)
				} else if (currentPos <= 0.0f) {
					view.setTranslationX(translationX)
				} else if (currentPos >= 0.5) { //两个view中间的临界，这时两个view在同一层
					view.setTranslationX(translationX - mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f)
				} else {
					view.setTranslationX(translationX)
				}
				view.setScaleX(scale + mScaleMin)
				view.setScaleY(scale + mScaleMin)
			} else {
				view.setScaleX(mScaleMin)
				view.setScaleY(mScaleMin)
				view.setTranslationX(-reduceX - mCoverWidth)
			}
		}
	}
}*/

class SS2 : ViewPager2.PageTransformer {
	override fun transformPage(view: View, position: Float) {
		view.apply {
			val pageWidth = width
			when {
				position < -1 -> { // [-Infinity,-1)
					// This page is way off-screen to the left.
					//alpha = 0f
				}
				position <= 0 -> { //[-1, 0]
					translationX = pageWidth * position

					updateLayoutParams<ViewGroup.MarginLayoutParams> {
						// 33 if 1 else if 0 then 0
						updateMargins(
							right = context.dpToPx((abs(1 - position) * 64f)).roundToInt(),
							left = context.dpToPx((abs(1 - position) * 64f)).roundToInt(),
						)
					}
				}
				position <= 1 -> { // (0,1] fade out z == -1
					translationX = pageWidth * -position
				}
				else -> { // (1,+Infinity]
					// This page is way off-screen to the right.
					//alpha = 0f
				}
			}
		}
	}
}
class SS1 : ViewPager2.PageTransformer {
	override fun transformPage(view: View, position: Float) {
		view.apply {
			if (true) {
				abs(position - 0.5)

				return
			}

			val pageWidth = width
			when {
				position == 0f -> {
					scaleX = 0.5f
					scaleY = 0.5f
				}
				//========================//=
				position < -1 -> { // [-Infinity,-1)
					// This page is way off-screen to the left.
					alpha = 0f
				}
				position <= 0 -> { // [-1,0]
					// Use the default slide transition when moving to the left page
					alpha = 1f
					translationX = 0f
					translationZ = 0f
					scaleX = 1f
					scaleY = 1f

					updateLayoutParams<ViewGroup.MarginLayoutParams> {
						// 33 if 1 else if 0 then 0
						updateMargins(
							right = context.dpToPx((abs(1 - position) * 64f)).roundToInt(),
						)
					}
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
					alpha = 0f
				}
			}
		}
	}
}

class DepthPageTransformer : ViewPager2.PageTransformer {

	override fun transformPage(view: View, position: Float) {
		view.apply {
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
					scaleX = 1f
					scaleY = 1f

					updateLayoutParams<ViewGroup.MarginLayoutParams> {
						// 33 if 1 else if 0 then 0
						updateMargins(
							right = context.dpToPx((abs(1 - position) * 64f)).roundToInt(),
						)
					}
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
					alpha = 0f
				}
			}
		}
	}
}

class PageTransformerBecomeShopPackages : ViewPager2.PageTransformer {

	// 33dp

	override fun transformPage(view: View, position: Float) {
		view.apply {
			val horzMargin = context.dpToPx((abs(1 - position) * 64f)).roundToInt()

			updateLayoutParams<ViewGroup.MarginLayoutParams> {
				// 33 if 1 else if 0 then 0
				updateMargins(
					top = context.dpToPx((abs(position) * 33f)).roundToInt(),
					left = horzMargin,
					right = horzMargin,
				)
			}

			val offset = position * 64f
			translationX = offset

			//elevation = context.dpToPx((abs(position) * 8f))

			// rob3 et7arako isa.

			/*val pageWidth = width
			val pageHeight = height
			when {
				position < -1 -> { // [-Infinity,-1)
					// This page is way off-screen to the left.
					alpha = 0f
				}
				position <= 1 -> { // [-1,1] // 0 means stable idle scroll state
					// Modify the default slide transition to shrink the page as well
					updateLayoutParams<ViewGroup.MarginLayoutParams> {
						// 33 if 1 else if 0 then 0
						updateMargins(
							top = context.dpToPx((abs(position) * 33f)).roundToInt()
						)
					}
					val scaleFactor = max(MIN_SCALE, 1 - abs(position))
					val vertMargin = pageHeight * (1 - scaleFactor) / 2
					val horzMargin = pageWidth * (1 - scaleFactor) / 2
					translationX = if (position < 0) {
						horzMargin - vertMargin / 2
					} else {
						horzMargin + vertMargin / 2
					}

					// Scale the page down (between MIN_SCALE and 1)
					scaleX = scaleFactor
					scaleY = scaleFactor

					// Fade the page relative to its size.
					alpha = (MIN_ALPHA +
						(((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
				}
				else -> { // (1,+Infinity]
					// This page is way off-screen to the right.
					alpha = 0f
				}
			}*/
		}
	}

}

/*
package com.example

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class OffsetPageTransformer(
    @Px private val offsetPx: Int,
    @Px private val pageMarginPx: Int
) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val viewPager = requireViewPager(page)
        val offset = position * -(2 * offsetPx + pageMarginPx)
        val totalMargin = offsetPx + pageMarginPx

        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            page.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = totalMargin
                marginEnd = totalMargin
            }

            page.translationX = if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                -offset
            } else {
                offset
            }
        } else {
            page.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = totalMargin
                bottomMargin = totalMargin
            }

            page.translationY = offset
        }
    }

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
}
 */
