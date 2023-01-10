package grand.app.moon.extensions.bindingAdapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
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

@BindingAdapter("imageView_setupWithGlideOrEmptyBA_uri")
fun ImageView.setupWithGlideOrEmptyBAUri(uri :Uri?) {
	if (uri == null) setImageResource(0) else {
		Glide.with(this)
			.load(uri)
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
	imageTintList = ColorStateList.valueOf(
		if (res == null) Color.TRANSPARENT else ContextCompat.getColor(context, res)
	)
}
