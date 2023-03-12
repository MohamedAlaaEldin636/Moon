package grand.app.moon.core.di.module

import grand.app.moon.extensions.MyLogger
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okio.*

object NetworkProgressModule {

	class ProgressInterceptor(private val listener: UploadProgressListener) : Interceptor {

		override fun intercept(chain: Interceptor.Chain): Response {
			val originalRequest = chain.request()

			MyLogger.e("NetworkProgressModule.UploadProgressListener originalRequest.body ${originalRequest.body}")

			if (originalRequest.body == null) {
				return chain.proceed(originalRequest)
			}

			MyLogger.e("NetworkProgressModule.UploadProgressListener originalRequest.body ch 2 ${originalRequest.body}")

			val progressRequest = originalRequest.newBuilder()
				.method(originalRequest.method, ProgressRequestBody(originalRequest.body!!, listener))
				.build()

			MyLogger.e("NetworkProgressModule.UploadProgressListener originalRequest.body ch 3 ${originalRequest.body}")

			return chain.proceed(progressRequest)
		}
	}

	class ProgressRequestBody(private val requestBody: RequestBody, private val listener: UploadProgressListener) : RequestBody() {

		override fun contentType(): MediaType? {
			return requestBody.contentType()
		}

		override fun contentLength(): Long {
			return requestBody.contentLength()
		}

		override fun writeTo(sink: BufferedSink) {
			val countingSink = CountingSink(sink, listener, contentLength())
			val bufferedSink = countingSink.buffer()

			requestBody.writeTo(bufferedSink)

			bufferedSink.flush()
		}
	}

	class CountingSink(delegate: BufferedSink, private val listener: UploadProgressListener, private val contentLength: Long) : ForwardingSink(delegate) {

		private var bytesWritten = 0L

		override fun write(source: Buffer, byteCount: Long) {
			super.write(source, byteCount)

			bytesWritten += byteCount

			val progress = (bytesWritten.toDouble() / contentLength.toDouble()) * 100

			listener.onProgressUpdate(progress.toInt())
		}
	}


	interface UploadProgressListener {
		fun onProgressUpdate(progress: Int)
	}




}
