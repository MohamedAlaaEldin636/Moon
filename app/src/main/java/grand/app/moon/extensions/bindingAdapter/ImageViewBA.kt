package grand.app.moon.extensions.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageView_setupWithGlideOrEmptyBA")
fun ImageView.setupWithGlideOrEmptyBA(url :String?) {
	setImageResource(0)

	if (!url.isNullOrEmpty()) {
		Glide.with(this)
			.load(url)
			.into(this)
	}
}
