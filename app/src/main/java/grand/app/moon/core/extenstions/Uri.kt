package grand.app.moon.core.extenstions

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import grand.app.moon.extensions.orZero
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*

/*
Log.e("aaa", "aaaaaaaaaaa 11 11 pre $paramNameInApi")
	val byteArray = toBytesArray(context) ?: return null
	Log.e("aaa", "aaaaaaaaaaa 11 22 pre $byteArray")
	val extension = getMimeType(context) ?: return null
	Log.e("aaa", "aaaaaaaaaaa 11 33 pre $extension")

	return MultipartBody.Part.createFormData(
		paramNameInApi, "File.$extension", byteArray.toRequestBody()
	)
 */

fun File.eeeeeeeeeeeee(paramNameInApi: String): MultipartBody.Part? {
	val byteArray = convertFileToByteArray() ?: return null
	val mimeType = getFileMimeType() ?: return null

	return MultipartBody.Part.createFormData(
		paramNameInApi, "File.$extension", byteArray.toRequestBody()
	)
}

fun File.getFileMimeType(): String? {
	val extension = MimeTypeMap.getFileExtensionFromUrl(path)
	return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

fun File.convertFileToByteArray(): ByteArray? {
	return kotlin.runCatching {
		val fis = FileInputStream(this)
		val byteArray = ByteArray(length().toInt())
		fis.read(byteArray)
		fis.close()
		byteArray
	}.getOrNull()
}

/*
val contentUri = Uri.parse("content://com.example.provider/myfile")
val inputStream = context.contentResolver.openInputStream(contentUri)
val file = File(context.cacheDir, "myfile")
val outputStream = FileOutputStream(file)

inputStream?.use { input ->
    outputStream.use { output ->
        input.copyTo(output)
    }
}

val fileUri = Uri.fromFile(file)
 */
fun Uri.ssssssssssss(context: Context): Uri {
	//scheme == ContentResolver.SCHEME_CONTENT

	val inputStream = context.contentResolver.openInputStream(this)
	val file = File(context.dataDir, "file_${System.currentTimeMillis()}")
	val outputStream = FileOutputStream(file)

	inputStream?.use { input ->
		outputStream.use { output ->
			input.copyTo(output)
		}
	}

	return Uri.fromFile(file)
}

fun Uri.getVideoLength(context: Context): Long {
	val retriever = MediaMetadataRetriever()
	retriever.setDataSource(context, this)
	val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
	val videoDuration = duration?.toLongOrNull().orZero()
	retriever.release()

	return videoDuration
}

private fun Uri.asFile() = if (path.isNullOrEmpty()) null else path?.let { File(it) }

fun Uri.createMultipartBodyPartAsFile(context: Context, paramNameInApi: String): MultipartBody.Part? {
	val file = asFile() ?: return null

	val extension = getMimeType(context) ?: return null

	return MultipartBody.Part.createFormData(
		paramNameInApi, "File.$extension", file.asRequestBody()
	)
}

fun Uri.createMultipartBodyPart(context: Context, paramNameInApi: String): MultipartBody.Part? {
	Log.e("aaa", "aaaaaaaaaaa 11 11 pre $paramNameInApi")
	val byteArray = toBytesArray(context) ?: return null
	Log.e("aaa", "aaaaaaaaaaa 11 22 pre $byteArray")
	val extension = getMimeType(context) ?: return null
	Log.e("aaa", "aaaaaaaaaaa 11 33 pre $extension")

	return MultipartBody.Part.createFormData(
		paramNameInApi, "File.$extension", byteArray.toRequestBody()
	)
}

private  val TAG = "Uri"
private fun Uri.toBytesArray(context: Context): ByteArray? {
	return try {
		val inputStream = context.applicationContext.contentResolver.openInputStream(this)
		
		return inputStream?.readBytes()
	}catch (throwable: Throwable) {
    Log.d(TAG, "toBytesArray: ${throwable.message}")

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
