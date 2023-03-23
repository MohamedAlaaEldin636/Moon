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
		setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // THREE_GPP // right .3gp
		setOutputFile(outputFilePath)
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			setOutputFile(outputFile)
		}else {
			setOutputFile(outputFile.absolutePath)
		}*/
		setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // left
		// AAC_ELD // MPEG_4 // .m4a
		// AMR_NB // THREE_GPP // .3gp

		try {
			prepare()
		} catch (throwable: Throwable) {
			MyLogger.e("prepare() of recorder failed $throwable")

			return null
		}

		//start()
	}
}
