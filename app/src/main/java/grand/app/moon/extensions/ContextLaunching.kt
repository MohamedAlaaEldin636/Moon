package grand.app.moon.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import grand.app.moon.R

@SuppressLint("HardwareIds")
fun Context.getDeviceIdWithoutPermission(fallbackOfNullOrEmpty: String = "device_id"): String {
	return kotlin.runCatching {
		Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
	}.getOrNull().let {
		if (it.isNullOrEmpty()) fallbackOfNullOrEmpty else it
	}
}

private fun Context.shareTextOnSpecificPackageOrBrowserIfPossibleOrAsPlainText(text: String, appPackage: String) {
	val shareIntent = Intent(Intent.ACTION_SEND)
	shareIntent.type = "text/plain"
	shareIntent.putExtra(Intent.EXTRA_TEXT, text)

	// Specify that we want to share via Facebook
	if (appPackage.isNotEmpty()) {
		shareIntent.setPackage(appPackage)
	}

	// Check if the Facebook app is installed on the device
	if (appPackage.isNotEmpty() && shareIntent.resolveActivity(packageManager) != null) {
		startActivity(shareIntent)
	}else {
		launchBrowserOrElse(
			text
		) {
			// Facebook app not installed, open web browser instead
			shareIntent.setPackage(null)
			launchActivitySafely {
				startActivity(Intent.createChooser(shareIntent, getString(R.string.pick_app)))
			}
		}
	}
}

fun Context.shareOnFacebook(text: String) = shareTextOnSpecificPackageOrBrowserIfPossibleOrAsPlainText(
	text, "com.facebook.katana"
)

fun Context.shareOnInstagram(text: String) = shareTextOnSpecificPackageOrBrowserIfPossibleOrAsPlainText(
	text, "com.instagram.android"
)

fun Context.shareOnTwitter(text: String) = shareTextOnSpecificPackageOrBrowserIfPossibleOrAsPlainText(
	text, "com.twitter.android"
)

fun Context.shareOnYoutube(text: String) = shareTextOnSpecificPackageOrBrowserIfPossibleOrAsPlainText(
	text, ""
)

fun Context.launchBrowserOrElse(link: String, fallback: () -> Unit) {
	val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
		.wrapInChooser(getString(R.string.pick_app))

	if (intent.resolveActivity(packageManager) != null) {
		startActivity(intent)
	}else {
		fallback()
	}
}

fun Context.launchOpenYoutubeApp() {
	val intent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
	if (intent != null) {
		startActivity(intent)
	} else {
		// YouTube app not installed, open the YouTube website in a browser
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"))
		startActivity(browserIntent)
	}
}
