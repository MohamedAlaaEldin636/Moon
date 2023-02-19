package com.grand.trim_video_lib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.postDelayed
import androidx.databinding.DataBindingUtil
import com.deep.videotrimmer.interfaces.OnTrimVideoListener
import com.deep.videotrimmer.utils.FileUtils
import com.grand.trim_video_lib.databinding.ActivityTrimmingVideoNum2Binding
import java.io.File
import java.io.FileOutputStream

class TrimmingVideoNum2Activity : AppCompatActivity() {

	companion object {
		internal const val INTENT_KEY_URI = "INTENT_KEY_URI"

		fun aaaaaaaaaaaaa(context: Context, uri: Uri) = FileUtils.getPath(context, uri)
	}

	lateinit var binding: ActivityTrimmingVideoNum2Binding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.activity_trimming_video_num_2)
		binding.lifecycleOwner = this

		val uri = Uri.parse(intent?.getStringExtra(INTENT_KEY_URI).orEmpty())
		binding.deepVideoTrimmer.setMaxDuration((uri.getVideoLength(this) / 1000).toInt().coerceAtLeast(1))
		Log.e("aa", "dofkpsdofk 2 $uri")
		val path = FileUtils.getPath(this, uri/*.ssssssssssss(this)*/)
		binding.deepVideoTrimmer.setVideoURI(Uri.parse(path)/*uri.ssssssssssss(this)*//*Uri.parse(path)*//*.ssssssssssss(this)*/)
		val destPath = "${Environment.getExternalStorageDirectory()}/MyApp/videos"
		//videoTrimmer.setDestinationPath(destPath)
		// destPath
		binding.deepVideoTrimmer.setDestinationPath(filesDir.absolutePath/*filesDir.absolutePath*/)
		binding.deepVideoTrimmer.setOnTrimVideoListener(object : OnTrimVideoListener {
			override fun getResult(uri: Uri?) {
				Log.e("abc", "diaosjdoaisjoajds ${TrimmingVideoNum2Activity::class.java.name} -> $uri")
				val intent = Intent().also {
					it.putExtra(INTENT_KEY_URI, uri.toString())
				}
				setResult(Activity.RESULT_OK, intent)
				finish()
			}

			override fun cancelAction() {
				Log.e("abc", "${TrimmingVideoNum2Activity::class.java.name} -> CANCEL")
				finish()
			}
		})
	}

	fun Uri.ssssssssssss(context: Context): Uri {
		//scheme == ContentResolver.SCHEME_CONTENT

		val inputStream = context.contentResolver.openInputStream(this)
		val file = File(context.filesDir, "file_${System.currentTimeMillis()}")
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
		val videoDuration = duration?.toLongOrNull() ?: 0L
		retriever.release()

		return videoDuration
	}

	class StartTrim : ActivityResultContract<Uri, Uri?>() {
		override fun createIntent(context: Context, input: Uri): Intent {
			return Intent(context, TrimmingVideoNum2Activity::class.java).also {
				it.putExtra(INTENT_KEY_URI, input.toString())
			}
		}

		override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
			return intent?.getStringExtra(INTENT_KEY_URI)?.let { Uri.parse(it) }
		}
	}

}
