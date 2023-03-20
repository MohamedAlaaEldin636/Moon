package ma.ya.cometchatintegration.extensions

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import ma.ya.cometchatintegration.helperClasses.MyLogger

fun Uri.checkSizeAndLengthOfVideo(context: Context, maxDurationInSeconds: Int): Boolean {
	MyLogger.e("deiwu -> pre")

	kotlin.runCatching {
		val retriever = MediaMetadataRetriever()
		// use one of overloaded setDataSource() functions to set your data source
		retriever.setDataSource(context, this)
		val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		val timeInMilliSeconds = time?.toLongOrNull()
		val durationTimeInSecond = timeInMilliSeconds?.let { it / 1000 }

		MyLogger.e("deiwu -> $durationTimeInSecond $timeInMilliSeconds")

		if (durationTimeInSecond != null && durationTimeInSecond > maxDurationInSeconds) {
			return false
		}
	}.getOrElse {
		MyLogger.e("ERROR dhsaiuhsa -> $it")

		return false
	}

	/*
	https://stackoverflow.com/a/63529377

	ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(imageUri, "r");
	fileLength = pfd.getStatSize();
	pfd.close();
	 */
	// todo -> https://stackoverflow.com/questions/49415012/get-file-size-using-uri-in-android
	//val fileSize = Integer.parseInt(String.valueOf((volleyFileObject.getFile().length() / 1024) / 1024));
	// https://stackoverflow.com/a/67251625

	return true
}
