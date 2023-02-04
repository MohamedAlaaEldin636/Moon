package com.grand.trim_video_lib

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.gowtham.library.ui.ActVideoTrimmer
import com.gowtham.library.utils.TrimVideo
import com.gowtham.library.utils.TrimVideoOptions

fun Fragment.registerForActivityResultTrimVideo(
	onResult: (Uri) -> Unit
) = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
		result: ActivityResult ->
	if (result.resultCode == Activity.RESULT_OK && result.data != null) {
		Log.e("aaa", "aaaaaaaaaaa 1")
		val uri = kotlin.runCatching {
			Uri.parse(TrimVideo.getTrimmedVideoPath(result.data))
		}.getOrNull()

		if (uri != null) {
			onResult(uri)
		}
	}else {
		Log.e("aaa", "aaaaaaaaaaa 2")
	}
}

fun Fragment.launchSafelyTrimVideo(uri: Uri, launcher: ActivityResultLauncher<Intent>, conversion: (Any?) -> String?) {
	kotlin.runCatching {
		launcher.launch(
			Intent(context, ActVideoTrimmer::class.java).also {
				/*
				Intent intent = new Intent(activity, ActVideoTrimmer.class);
            Gson gson = new Gson();
            Bundle bundle = new Bundle();
            bundle.putString("trim_video_uri", this.videoUri);
            bundle.putString("trim_video_option", gson.toJson(this.options));
            intent.putExtras(bundle);
            return intent;
				 */

				val bundle = bundleOf(
					"trim_video_uri" to uri.toString(),
					"trim_video_option" to conversion(TrimVideoOptions().also { options ->
						options.hideSeekBar = true
					})
				)

				it.putExtras(bundle)
			}
		)

		if (true) return@runCatching

		TrimVideo.activity(uri.toString())
			//.setCompressOption(new CompressOption()) //empty constructor for default compress option
			//.setLocal(requireContext().filesDir.path)
			.setHideSeekBar(true)
			.start(this, launcher)
	}
}
