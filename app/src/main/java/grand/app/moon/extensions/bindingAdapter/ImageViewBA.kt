package grand.app.moon.extensions.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import grand.app.moon.R

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
