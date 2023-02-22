package grand.app.moon.extensions.bindingAdapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import grand.app.moon.R
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.extensions.compose.GlideImageViaXmlModel
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.saveDiskCacheStrategyAll
import grand.app.moon.extensions.setupWithGlide

@BindingAdapter("imageView_setImageDrawableBA")
fun ImageView.setImageDrawableBA(drawable: Drawable?) {
	setImageDrawable(drawable)
}

@BindingAdapter("imageView_setupWithGlideOrDefaultImageBA")
fun ImageView.setupWithGlideOrDefaultImageBA(value: String?) {
	if (value.isNullOrEmpty().not()) {
		setupWithGlide {
			load(value).saveDiskCacheStrategyAll()
		}
	}else {
		setImageResource(R.drawable.ic_defaulf_image)
	}
}

@BindingAdapter("imageView_setupWithGlideOrDefaultUserBA")
fun ImageView.setupWithGlideOrDefaultUserBA(value: String?) {
	if (value.isNullOrEmpty().not()) {
		setupWithGlide {
			load(value).saveDiskCacheStrategyAll()
		}
	}else {
		setImageResource(R.drawable.ic_default_user)
	}
}

@BindingAdapter(
	"imageView_setupWithGlideWithDefaultImage_value",
	"imageView_setupWithGlideWithDefaultImage_ignore",
	requireAll = true
)
fun ImageView.setupWithGlideWithDefaultImage(value: String?, ignore: Boolean?) {
	if (ignore?.not().orFalse()) {
		setupWithGlide {
			load(value).saveDiskCacheStrategyAll()
		}
	}
}

@BindingAdapter("imageView_setupWithGlideWithDefaultsPlaceholderAndError")
fun ImageView.setupWithGlideWithDefaultsPlaceholderAndError(url: String?) {
	Glide.with(this)
		.load(url)
		.placeholder(R.drawable.ic_baseline_refresh_24)
		.error(R.drawable.ic_baseline_broken_image_24)
		.into(this)
}

@BindingAdapter("imageView_setupWithGlideWithDefaultsPlaceholderAndError_video")
fun ImageView.setupWithGlideWithDefaultsPlaceholderAndErrorVideo(url: String?) {
	Glide.with(this)
		.load(url)
		.apply(RequestOptions().frame(1)/*.centerCrop()*/)
		.placeholder(R.drawable.ic_baseline_refresh_24)
		.error(R.drawable.ic_baseline_broken_image_24)
		.into(this)
}

@BindingAdapter("imageView_setupWithGlideWithDefaultsPlaceholderAndError_listImagesOrVideo")
fun ImageView.setupWithGlideWithDefaultsPlaceholderAndErrorListImagesOrVideo(urls: List<String>?) {
	Glide.with(this)
		.load(urls?.firstOrNull())
		.placeholder(R.drawable.ic_baseline_refresh_24)
		.error(R.drawable.ic_baseline_broken_image_24)
		.into(this)
}

@BindingAdapter("imageView_setupWithGlideOrIgnoreMAImagesOrVideo")
fun ImageView.setupWithGlideOrIgnoreMAImagesOrVideo(model: MAImagesOrVideo?) {
	when (model) {
		is MAImagesOrVideo.Images -> {
			Glide.with(this)
				.load(model.images.firstOrNull() ?: return)
				.into(this)
		}
		is MAImagesOrVideo.Video -> {
			Glide.with(this)
				.load(model.video)
				.apply(RequestOptions().frame(1)/*.centerCrop()*/)
				.into(this)
		}
		null -> {}
	}
}

@BindingAdapter("imageView_setupWithGlideOrIgnoreGlideImageViaXmlModelBA")
fun ImageView.setupWithGlideOrIgnoreGlideImageViaXmlModelBA(model: GlideImageViaXmlModel?) {
	when (model) {
		is GlideImageViaXmlModel.IString -> if (model.string.isNullOrEmpty().not()) {
			Glide.with(this)
				.load(model.string)
				.into(this)
		}
		is GlideImageViaXmlModel.IUri -> if (model.uri != null) {
			Glide.with(this)
				.load(model.uri)
				.into(this)
		}
		null -> {}
	}
}

@BindingAdapter("imageView_setupWithGlideOrEmptyBA")
fun ImageView.setupWithGlideOrEmptyBA(url :String?) {
	setImageResource(0)

	if (!url.isNullOrEmpty()) {
		Glide.with(this)
			.load(url)
			.into(this)
	}
}

@BindingAdapter("imageView_setupWithGlideOrIgnore_drawableResName")
fun ImageView.setupWithGlideOrIgnoreDrawableResName(name :String?) {
	val id = context?.resources?.getIdentifier(name, "drawable", context?.packageName).orZero()

	if (id != 0) {
		Glide.with(this)
			.load(id)
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
fun ImageView.setupWithGlideOrSplashBA(url: String?) {
	Glide.with(this)
		.load(R.drawable.splash)
		.into(this)

	if (!url.isNullOrEmpty()) {
		Glide.with(this)
			.load(url)
			.into(this)
	}
}

@BindingAdapter("imageView_setupWithGlideOrElseResNameBA_url", "imageView_setupWithGlideOrElseResNameBA_resName", requireAll = true)
fun ImageView.setupWithGlideOrElseResNameBA(url: String?, resName: String) {
	val id = context?.resources?.getIdentifier(resName, "drawable", context?.packageName).orZero()

	Glide.with(this)
		.load(id)
		.into(this)

	if (!url.isNullOrEmpty()) {
		Glide.with(this)
			.load(url)
			.into(this)
	}
}

@BindingAdapter("imageView_setupWithGlideResIdOrSplashBA")
fun ImageView.setupWithGlideResIdOrSplashBA(resId: Int?) {
	Glide.with(this)
		.load(R.drawable.splash)
		.into(this)

	if (resId != null) {
		Glide.with(this)
			.load(resId)
			.into(this)
	}
}

@BindingAdapter("imageView_setTintResBA")
fun ImageView.setTintBA(@ColorRes res: Int?) {
	imageTintList = ColorStateList.valueOf(
		if (res == null) Color.TRANSPARENT else ContextCompat.getColor(context, res)
	)
}
