package com.grand.trim_video_lib

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.gowtham.library.utils.TrimVideo

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

fun Fragment.launchSafelyTrimVideo(uri: Uri, launcher: ActivityResultLauncher<Intent>) {
	kotlin.runCatching {
		TrimVideo.activity(uri.toString())
			//.setCompressOption(new CompressOption()) //empty constructor for default compress option
			//.setLocal(requireContext().filesDir.path)
			.setHideSeekBar(true)
			.start(this, launcher)
	}
}
