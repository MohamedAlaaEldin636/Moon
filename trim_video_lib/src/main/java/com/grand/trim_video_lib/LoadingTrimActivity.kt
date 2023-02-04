package com.grand.trim_video_lib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import com.google.gson.Gson
import com.gowtham.library.ui.ActVideoTrimmer
import com.gowtham.library.utils.TrimVideo
import com.gowtham.library.utils.TrimVideoOptions

class LoadingTrimActivity : AppCompatActivity() {

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK && result.data != null) {
			val uri = kotlin.runCatching {
				Uri.parse(TrimVideo.getTrimmedVideoPath(result.data))
			}.getOrNull()

			if (uri != null) {
				val intent = Intent().also {
					it.putExtra(MAActivityResultContracts.INTENT_KEY_URI, uri.toString())
				}
				setResult(Activity.RESULT_OK, intent)
				Handler(Looper.getMainLooper()).postDelayed(1_000) {
					finish()
				}
				//finish()
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_loading_trim)

		Handler(Looper.getMainLooper()).postDelayed(1_000) {
			TrimVideo.activity(intent?.getStringExtra(MAActivityResultContracts.INTENT_KEY_URI))
				.setHideSeekBar(true)
				.start(this, launcher)
		}
	}
}

object MAActivityResultContracts {

	internal const val INTENT_KEY_URI = "INTENT_KEY_URI"

	class StartTrim : ActivityResultContract<Uri, Uri?>() {
		override fun createIntent(context: Context, input: Uri): Intent {
			return Intent(context, LoadingTrimActivity::class.java).also {
				it.putExtra(INTENT_KEY_URI, input.toString())
			}
		}

		override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
			return intent?.getStringExtra(INTENT_KEY_URI)?.let { Uri.parse(it) }
		}
	}

	/*class StartTrim : ActivityResultContract<Uri, Uri?>() {
		override fun createIntent(context: Context, input: Uri): Intent {
			return Intent(context, ActVideoTrimmer::class.java).also {
				val options = TrimVideoOptions().also { options ->
					options.hideSeekBar = true
				}

				val bundle = bundleOf(
					"trim_video_uri" to input.toString(),
					"trim_video_option" to Gson().toJson(options)
				)

				it.putExtras(bundle)
			}
		}

		override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
			Log.e("aaa", "qqqqqqqqqqqqqqqqq 1")
			if (resultCode == Activity.RESULT_OK && intent != null) {
				Log.e("aaa", "qqqqqqqqqqqqqqqqq 2")
				return kotlin.runCatching {
					Uri.parse(TrimVideo.getTrimmedVideoPath(intent))
				}.getOrNull()
			}

			return null
		}
	}*/

}
