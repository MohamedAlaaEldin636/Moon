package ma.ya.cometchatintegration.helperClasses

import android.util.Log

object MyLogger {

	private const val MY_LOGGER = "MY_LOGGER"

	@JvmStatic
	fun e(any: Any?) {
		Log.e(MY_LOGGER, "$MY_LOGGER -> $any")
	}

}
