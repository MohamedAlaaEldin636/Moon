package grand.app.moon.core.extenstions

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty
import grand.app.moon.R
import grand.app.moon.core.MyApplication

fun Context.showError( message: String) {
  try{
    Toasty.error(this, message, Toast.LENGTH_SHORT, true).show();
  }catch (e: Exception){
    Toasty.error(MyApplication.instance, message, Toast.LENGTH_SHORT, true).show();
  }
}

fun Context.showInfo( message: String) {
  Toasty.info(this, message, Toast.LENGTH_SHORT, true).show();
}

fun Context.toast(msg: String) {
	Toast.makeText(
		this,
		msg,
		Toast.LENGTH_SHORT
	).show()
}
