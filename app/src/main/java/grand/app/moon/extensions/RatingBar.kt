package grand.app.moon.extensions

import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.BindingAdapter
import kotlin.math.roundToInt

@BindingAdapter("appCompatRatingBar_setProgress")
fun AppCompatRatingBar.setProgressBA(progress: Int?) {
	val actualProgress = (progress ?: 0).coerceAtLeast(0)
		.coerceAtMost(100)

	max = 100
	this.progress = actualProgress
}

@BindingAdapter("appCompatRatingBar_setProgressFloat")
fun AppCompatRatingBar.setProgressBAFloat(progress: Float?) {
	val actualProgress = (progress?.roundToInt() ?: 0).coerceAtLeast(0)
		.coerceAtMost(100)

	max = 100
	stepSize = 1f
	this.progress = actualProgress
}
