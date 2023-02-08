package grand.app.moon.extensions

import android.content.Context
import android.widget.ImageView
import androidx.annotation.FloatRange
import androidx.core.view.postDelayed
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import coil.load
import coil.request.videoFramePercent

object MACoil {

	private var videoImageLoader: ImageLoader? = null

	@JvmStatic
	fun videoImageLoader(context: Context): ImageLoader = videoImageLoader ?: newImageLoader(context)

	@Synchronized
	private fun newImageLoader(context: Context): ImageLoader {
		// Check again in case imageLoader was just set.
		videoImageLoader?.let { return it }

		// Create a new ImageLoader.
		val newImageLoader = ImageLoader.Builder(context)
			.components {
				add(VideoFrameDecoder.Factory())
			}
			.build()
		videoImageLoader = newImageLoader
		return newImageLoader
	}

}

fun ImageView.loadVideoFrame(url: String, @FloatRange(from = 0.0, to = 1.0) framePercent: Double = 0.0) {
	load(url, MACoil.videoImageLoader(context ?: return)) {
		videoFramePercent(framePercent)
	}
}

fun ImageView.loadVideoFrames(url: String, framePercent: Double = 0.1) {
	/*val request = ImageRequest.Builder(context)
		.data(url)
		.videoFramePercent(0.1)
		.target(
			this
		).target(
			onStart = { placeholder ->
				// Handle the placeholder drawable.
			},
			onSuccess = { result ->
				// Handle the successful result.
			},
			onError = { error ->
				// Handle the error drawable.
			}
		)
		.build()
	MACoil.videoImageLoader(context ?: return).enqueue(request)*/
	load(url, MACoil.videoImageLoader(context ?: return)) {
		videoFramePercent(framePercent)

		target(
			onStart = {
				setImageResource(0)
			},
			onSuccess = {
				postDelayed(1_000) {
					loadVideoFrames(url, framePercent.plus(0.1).let {
						if (it > 1.0) 0.0 else it
					})
				}
			}
		)
	}
}
