package codes.grand.images_slider.utils

import android.content.res.Resources.getSystem
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import grand.images_slider.R

internal fun ImageView.loadSliderImage(imageUrl: String?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    Glide
      .with(context)
      .load(imageUrl)
      .placeholder(circularProgressDrawable)
      .error(R.drawable.bg_no_image)
      .into(this)
  } else {
    Glide
      .with(context)
      .clear(this)

    setImageResource(R.drawable.bg_no_image)
  }
}

val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()