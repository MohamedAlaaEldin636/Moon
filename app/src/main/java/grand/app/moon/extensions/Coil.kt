package grand.app.moon.extensions

import android.content.Context
import android.widget.ImageView
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
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

fun ImageView.loadVideoFrame(url: String) {
	load(url, MACoil.videoImageLoader(context ?: return)) {
		videoFramePercent(0.1)
	}
}
