package grand.app.moon.core.extenstions

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import es.dmoral.toasty.Toasty
import grand.app.moon.R

fun Context.showError(message: String) {
  try{
	  //Toasty.error(this, message, Toast.LENGTH_SHORT, true).show();
	  Toasty.custom(
		  this,
		  message,
		  ContextCompat.getDrawable(this, R.drawable.ic_clear_white_24dp),
		  ContextCompat.getColor(this, R.color.no_button_color_in_dialogs),
		  ContextCompat.getColor(this, R.color.defaultTextColor),
		  Toast.LENGTH_SHORT,
		  true,
		  true
	  ).show()
  }catch (e: Exception){
    //Toasty.error(MyApplication.instance, message, Toast.LENGTH_SHORT, true).show();
	  Toasty.custom(
		  this,
		  message,
		  ContextCompat.getDrawable(this, R.drawable.ic_clear_white_24dp),
		  ContextCompat.getColor(this, R.color.no_button_color_in_dialogs),
		  ContextCompat.getColor(this, R.color.defaultTextColor),
		  Toast.LENGTH_SHORT,
		  true,
		  true
	  ).show()
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
