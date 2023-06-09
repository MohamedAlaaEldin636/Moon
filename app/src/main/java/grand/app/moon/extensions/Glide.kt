package grand.app.moon.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.postDelayed
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.Downsampler
import com.bumptech.glide.load.resource.bitmap.ParcelFileDescriptorBitmapDecoder
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder
import com.bumptech.glide.load.resource.bitmap.VideoDecoder
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import grand.app.moon.R
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun getBitmapFromURL(src: String?): Bitmap? {
	return try {
		val url = URL(src)
		val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
		connection.setDoInput(true)
		connection.connect()
		val input: InputStream = connection.getInputStream()
		BitmapFactory.decodeStream(input)
	} catch (e: IOException) {
		e.printStackTrace()
		null
	}
}

suspend fun Context.getBitmapFromURLUsingGlide(src: String?, sizeInPx: Int? = null): Bitmap? {
	return Glide.with(this@getBitmapFromURLUsingGlide)
		.asBitmap()
		.load(src)
		.let {
			if (sizeInPx.orZero() <= 0) it else {
				it.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
					.override(sizeInPx.orZero(), sizeInPx.orZero()))
			}
		}
		.intoBitmap()
}

suspend fun RequestBuilder<Bitmap>.intoBitmap(): Bitmap? {
	return suspendCoroutine { continuation ->
		into(object : CustomTarget<Bitmap>() {
			override fun onResourceReady(
				resource: Bitmap,
				transition: Transition<in Bitmap>?
			) {
				kotlin.runCatching {
					continuation.resume(resource)
				}.getOrElse {
					kotlin.runCatching {
						continuation.resume(null)
					}
				}
			}

			override fun onLoadFailed(errorDrawable: Drawable?) {
				kotlin.runCatching {
					continuation.resume(null)
				}
			}

			override fun onLoadCleared(placeholder: Drawable?) {
				kotlin.runCatching {
					continuation.resume(placeholder?.toBitmap())
				}
			}
		})
	}
}

fun <TranscodeType> RequestBuilder<TranscodeType>.saveDiskCacheStrategyAll(): RequestBuilder<TranscodeType> =
	apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))

fun ImageView.clearWithGlide() {
	Glide.with(this)
		.clear(this)
}

fun <TranscodeType> ImageView.setupWithGlide(
	modifications: RequestManager.() -> RequestBuilder<TranscodeType>
) {
	Glide.with(this)
		.modifications()
		.into(this)
}

/*private fun Context.f() {
	val bitmapPool: BitmapPool = Glide.get(getApplicationContext()).bitmapPool
	val microSecond = 6000000 // 6th second as an example

	val videoBitmapDecoder = VideoDecoder.parcel(bitmapPool)
	Downsampler(videoBitmapDecoder, resources.displayMetrics, bitmapPool, DecodeFormat.PREFER_ARGB_8888)
	val fileDescriptorBitmapDecoder =
		ParcelFileDescriptorBitmapDecoder(videoBitmapDecoder, bitmapPool, DecodeFormat.PREFER_ARGB_8888)
	Glide.with(getApplicationContext())
		.load(yourUri)
		.asBitmap()
		.override(50, 50) // Example
		.videoDecoder(fileDescriptorBitmapDecoder)
		.into(yourImageView)
}*/

fun ImageView.loadAsVideo(videoUrl: String?, timeOfFramesDelayInMs: Long = 1_000, frame: Long = 3_000L) {
	MyLogger.e("loadAsVideo -> frame $frame")
	val weakRefImageView = WeakReference(this)
	Glide.with(this)
		.load(videoUrl)
		.apply(RequestOptions().frame(frame).centerCrop())
		.listener(
			object : RequestListener<Drawable> {
				override fun onLoadFailed(
					e: GlideException?,
					model: Any?,
					target: Target<Drawable>?,
					isFirstResource: Boolean
				): Boolean {
					weakRefImageView.get()?.postDelayed(timeOfFramesDelayInMs) {
						weakRefImageView.get()?.loadAsVideo(videoUrl, timeOfFramesDelayInMs, frame + 24L)
					}
					return false
				}

				override fun onResourceReady(
					resource: Drawable?,
					model: Any?,
					target: Target<Drawable>?,
					dataSource: DataSource?,
					isFirstResource: Boolean
				): Boolean {
					weakRefImageView.get()?.postDelayed(timeOfFramesDelayInMs) {
						weakRefImageView.get()?.loadAsVideo(videoUrl, timeOfFramesDelayInMs, frame + 24L)
					}
					return false
				}
			}
		)
		.into(this)
}

fun <TranscodeType> RequestBuilder<TranscodeType>.asVideo(frame: Long = 1): RequestBuilder<TranscodeType> =
	asVideoIfRequired(true, frame)

fun <TranscodeType> RequestBuilder<TranscodeType>.asVideoIfRequired(isVideo: Boolean, frame: Long = 1): RequestBuilder<TranscodeType> =
	run { if (isVideo.not()) this else apply(RequestOptions().frame(frame).centerCrop()) }

fun ImageView.loadVideoWithChangedFrameUsingGlide(videoLink: String?, frame: Long = 1L) {
	val listener = object : RequestListener<Drawable> {
		override fun onLoadFailed(
			e: GlideException?,
			model: Any?,
			target: Target<Drawable>?,
			isFirstResource: Boolean
		): Boolean {
			kotlin.runCatching {
				post {
					kotlin.runCatching {
						this@loadVideoWithChangedFrameUsingGlide.loadVideoWithChangedFrameUsingGlide(
							videoLink, 1L
						)
					}
				}
			}
			return true
		}

		override fun onResourceReady(
			resource: Drawable?,
			model: Any?,
			target: Target<Drawable>?,
			dataSource: DataSource?,
			isFirstResource: Boolean
		): Boolean {
			kotlin.runCatching {
				postDelayed(1_000) {
					kotlin.runCatching {
						Glide.with(this@loadVideoWithChangedFrameUsingGlide)
							.load(R.drawable.ic_default_user)
							.into(this@loadVideoWithChangedFrameUsingGlide)

						postDelayed(1_000) {
							kotlin.runCatching {
								this@loadVideoWithChangedFrameUsingGlide.loadVideoWithChangedFrameUsingGlide(
									videoLink, frame + 60L
								)
							}
						}
					}
				}
			}
			return false
		}
	}

	Glide.with(this)
		.load(videoLink)
		.apply(RequestOptions().frame(frame).centerCrop())
		.listener(listener)
		.into(this)
}
