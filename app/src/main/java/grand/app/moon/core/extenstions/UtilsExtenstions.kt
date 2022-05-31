package grand.app.moon.core.extenstions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import es.dmoral.toasty.Toasty
import grand.app.moon.core.MyApplication
import java.util.*
import androidx.core.content.ContextCompat.startActivity
import grand.app.moon.R
import java.lang.Exception
import java.net.URLEncoder


fun Context.sendMail( mail: String,subject: String,text:String) {
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mail))
  intent.putExtra(Intent.EXTRA_SUBJECT, subject)
  intent.putExtra(Intent.EXTRA_TEXT,text )
  startActivity(intent)
}

fun Context.callPhone( phone: String) {
  val call = Uri.parse("tel:$phone")
  val surf = Intent(Intent.ACTION_DIAL, call)
  startActivity(surf)
}

fun Context.navigateMap( lat: Double , lng: Double) {
  val uri: String = java.lang.String.format(
    Locale.ENGLISH,
    "http://maps.google.com/maps?q=loc:%f,%f",
    lat,
    lng
  )
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
  startActivity(intent)
}


fun Context.shareWhatsapp(title: String?, desc: String?, phone: String) {
  val titleWhatsapp = when(title){
    null -> ""
    else -> title
  }
  val description = when(desc){
    null -> ""
    else -> desc
  }
  var url = "https://api.whatsapp.com/send?phone=${phone}"
  val i = Intent(Intent.ACTION_VIEW)
  url += "&text=" + URLEncoder.encode(
    titleWhatsapp + "\n" + description,
    "UTF-8"
  )
  try {
    i.setPackage("com.whatsapp")
    i.data = Uri.parse(url)
    startActivity(i)
  } catch (e: Exception) {
    try {
      i.setPackage("com.whatsapp.w4b")
      i.data = Uri.parse(url)
      startActivity(i)
    } catch (exception: Exception) {
      showInfo(getString(R.string.please_install_whatsapp_on_your_phone));
    }
  }
}
