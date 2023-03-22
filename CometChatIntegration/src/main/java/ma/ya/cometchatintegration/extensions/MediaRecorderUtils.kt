package ma.ya.cometchatintegration.extensions

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import ma.ya.cometchatintegration.helperClasses.MyLogger

fun Context.createMediaRecorderOrNull(outputFilePath: String): MediaRecorder? {
	val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		MediaRecorder(this)
	}else {
		@Suppress("DEPRECATION")
		(MediaRecorder())
	}
	return recorder.apply {
		setAudioSource(MediaRecorder.AudioSource.MIC)
		setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
		setOutputFile(outputFilePath)
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			setOutputFile(outputFile)
		}else {
			setOutputFile(outputFile.absolutePath)
		}*/
		setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

		try {
			prepare()
		} catch (throwable: Throwable) {
			MyLogger.e("prepare() of recorder failed $throwable")

			return null
		}

		//start()
	}
}
