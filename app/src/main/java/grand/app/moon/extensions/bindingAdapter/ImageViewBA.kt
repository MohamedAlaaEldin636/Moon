package grand.app.moon.extensions.bindingAdapter

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import grand.app.moon.R
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.base.extensions.setTint

@BindingAdapter("imageView_setupWithGlideOrEmptyBA")
fun ImageView.setupWithGlideOrEmptyBA(url :String?) {
	setImageResource(0)

	if (!url.isNullOrEmpty()) {
		Glide.with(this)
			.load(url)
			.into(this)
	}
}

@BindingAdapter("imageView_setupWithGlideOrSplashBA")
fun ImageView.setupWithGlideOrSplashBA(url :String?) {
	Glide.with(this)
		.load(R.drawable.splash)
		.into(this)

	if (!url.isNullOrEmpty()) {
		Glide.with(this)
			.load(url)
			.into(this)
	}
}

@BindingAdapter("imageView_setTintResBA")
fun ImageView.setTintBA(@ColorRes res: Int?) {
	imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, res.orZero()))
}
