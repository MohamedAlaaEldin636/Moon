package grand.app.moon.core.extenstions

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty
import grand.app.moon.core.MyApplication

fun Context.showError( message: String) {
  Toasty.error(this, message, Toast.LENGTH_SHORT, true).show();
}

fun Context.showInfo( message: String) {
  Toasty.info(this, message, Toast.LENGTH_SHORT, true).show();
}