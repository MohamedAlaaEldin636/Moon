package grand.app.moon.extensions

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

fun Uri.createMultipartBodyPart(context: Context, paramNameInApi: String): MultipartBody.Part? {
	val byteArray = toBytesArray(context) ?: return null
	val extension = getMimeType(context) ?: return null
	
	return MultipartBody.Part.createFormData(
		paramNameInApi, "File.$extension", byteArray.toRequestBody()
	)
}

private fun Uri.toBytesArray(context: Context): ByteArray? {
	return try {
		val inputStream = context.applicationContext.contentResolver.openInputStream(this)
		
		return inputStream?.readBytes()
	}catch (throwable: Throwable) {
		MyLogger.e("error converting uri to byte[] $throwable")
		
		null
	}
}

private fun InputStream.readBytes(): ByteArray {
	// this dynamically extends to take the bytes you read
	val byteBuffer = ByteArrayOutputStream()
	
	// this is storage overwritten on each iteration with bytes
	val buffer = ByteArray(1024)
	
	// we need to know how may bytes were read to write them to the byteBuffer
	var length: Int
	while (read(buffer).also { length = it } != -1) {
		byteBuffer.write(buffer, 0, length)
	}
	
	// and then we can return your byte array.
	return byteBuffer.toByteArray()
}

private fun Uri.getMimeType(context: Context): String? {
	return if (scheme == ContentResolver.SCHEME_CONTENT) {
		// If scheme is a content
		MimeTypeMap.getSingleton()?.getExtensionFromMimeType(context.contentResolver.getType(this))
	}else {
		// If scheme is a File
		// This will replace white spaces with %20 and also other special characters.
		// This will avoid returning null values on file name with spaces and special characters.
		MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(path ?: return null))?.toString())
	}
}

/**
 * @return true if seconds is 15 or less & 10 mb or less
 */
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

/*
	public static String getNameOfPdfUriAndCheckSize(Context context, Uri uri) {
	String name = null;
	Cursor cursor = null;
	try {
		cursor = context.getApplicationContext().getContentResolver().query(uri, null, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			// in case require mimeType DocumentsContract.Document.COLUMN_MIME_TYPE
			// todo check about 30 MB of DocumentsContract.Document.COLUMN_SIZE;
			name = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));
		}
	}catch (Exception e) {
		Log.e("UriUtils", "could not get pdf file name " + e);
	}finally {
		if (cursor != null) {
			cursor.close();
		}
	}

	return name;
}
 */
