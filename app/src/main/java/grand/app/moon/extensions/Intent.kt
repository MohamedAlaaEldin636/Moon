package grand.app.moon.extensions

import android.content.*
import android.net.Uri
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.getSystemService
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError
import grand.app.moon.presentation.base.utils.showMessage


fun Intent.createChooserMA(title: CharSequence) = Intent.createChooser(this, title)

fun Context.launchTelegram() {
	val intent = packageManager?.getLaunchIntentForPackage("org.telegram.messenger")
	
	if (intent == null) {
		showError(getString(R.string.app_not_found))
	}
	
	launchActivitySafely {
		if (intent != null) {
			startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.launchDialNumber(phoneNumber: String) {
	val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber.trim()}"))
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.launchSendEMail(email: String, subject: String? = null, text: String? = null) {
	val intent = Intent(Intent.ACTION_SENDTO).also { intent ->
		intent.data = Uri.parse("mailto:"); // only email apps should handle this
		intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email));
		if (subject.isNullOrEmpty().not()) {
			intent.putExtra(Intent.EXTRA_SUBJECT, subject.orEmpty())
		}
		if (text.isNullOrEmpty().not()) {
			intent.putExtra(Intent.EXTRA_TEXT, text.orEmpty())
		}
	}

	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.launchWhatsApp(phoneNumber: String) {
	/*
	void openWhatsappContact(String number) {
    Uri uri = Uri.parse("smsto:" + number);
    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
    i.setPackage("com.whatsapp");
    startActivity(Intent.createChooser(i, ""));
}
	 */
	val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${phoneNumber.trim()}"))
	intent.`package` = "com.whatsapp"

	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

/*
val intent = Intent(Intent.ACTION_SEND)
    /*This will be the actual content you wish you share.*/
    /*This will be the actual content you wish you share.*/
//    val shareBody = message
    /*The type of the content is text, obviously.*/
    /*The type of the content is text, obviously.*/intent.type = "text/plain"
    /*Applying information Subject and Body.*/
    /*Applying information Subject and Body.*/intent.putExtra(
      Intent.EXTRA_SUBJECT,
      context.getString(R.string.app_name)
    )
    intent.putExtra(Intent.EXTRA_TEXT, title + "\n" + message)
    /*Fire!*/
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
 */

fun Context.launchShareText(title: String, msg: String) = launchShareText("$title\n$msg")

fun Context.launchShareText(text: String) {
	val intent = Intent(Intent.ACTION_SEND).also {
		it.type = "text/plain"
		it.putExtra(Intent.EXTRA_TEXT, text)
	}
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.copyToClipboard(userVisibleLabel: String, actualTextInClip: String = userVisibleLabel) {
	val clipboard = getSystemService<ClipboardManager>() ?: return

	val clip = ClipData.newPlainText(userVisibleLabel, actualTextInClip)
	clipboard.setPrimaryClip(clip)

	showMessage(this, getString(R.string.copy_is_done_successfully))
}

fun Context.launchBrowser(link: String) {
	if (link.isEmpty()) {
		return
	}

	val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Intent.wrapInChooser(title: CharSequence): Intent {
	return Intent.createChooser(this, title)
}

fun Context.launchAppOnGooglePlay() {
	launchActivitySafely {
		try {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).wrapInChooser(getString(R.string.pick_app)))
		}catch (e: ActivityNotFoundException) {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getAppWebLinkOnGooglePay())).wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.launchProviderAppOnGooglePlay() {
	val packageName = "com.grand.hassan.provider2"
	launchActivitySafely {
		try {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).wrapInChooser(getString(R.string.pick_app)))
		}catch (e: ActivityNotFoundException) {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getAppWebLinkOnGooglePay())).wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.getAppWebLinkOnGooglePay(): String {
	return "https://play.google.com/store/apps/details?id=$packageName"
}

fun Context.launchActivitySafely(msg: String = getString(R.string.something_went_wrong_please_try_again), block: () -> Unit) {
	try {
		block()
	}catch (throwable: Throwable) {
		showError(msg)
	}
}
