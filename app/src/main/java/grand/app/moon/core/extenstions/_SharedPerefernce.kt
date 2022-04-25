package grand.app.moon.core.extenstions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import grand.app.moon.core.MyApplication
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.presentation.auth.AuthActivity


fun Context.isLogin() : Boolean {
  val appPreferences: SharedPreferences =
    getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)
  return appPreferences.getInt("id", -1) != -1

}

fun Context.isLoginWithOpenAuth() : Boolean {
  val appPreferences: SharedPreferences =
    getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)
  val isLogin = appPreferences.getInt("id", -1) != -1
  if(!isLogin) startActivity(Intent(this, AuthActivity::class.java))
  return isLogin
}
