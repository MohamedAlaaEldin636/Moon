package grand.app.moon.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

fun <TranscodeType> ImageView.setupWithGlide(
	modifications: RequestManager.() -> RequestBuilder<TranscodeType>
) {
	Glide.with(this)
		.modifications()
		.into(this)
}

fun <TranscodeType> RequestBuilder<TranscodeType>.asVideoIfRequired(isVideo: Boolean): RequestBuilder<TranscodeType> =
	run { if (isVideo.not()) this else apply(RequestOptions().frame(1)) }
//						.let { if (item.isVideo.not()) it else it.apply(RequestOptions().frame(1)) }
