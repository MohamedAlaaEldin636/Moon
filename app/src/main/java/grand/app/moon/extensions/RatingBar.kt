package grand.app.moon.extensions

import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.BindingAdapter

@BindingAdapter("appCompatRatingBar_setProgress")
fun AppCompatRatingBar.setProgressBA(progress: Int?) {
	val actualProgress = (progress ?: 0).coerceAtLeast(0)
		.coerceAtMost(100)

	max = 100
	this.progress = actualProgress
}
