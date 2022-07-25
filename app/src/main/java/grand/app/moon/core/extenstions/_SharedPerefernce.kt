package grand.app.moon.core.extenstions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import com.google.gson.Gson
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.utils.Constants


fun Context.isLogin(): Boolean {
  val appPreferences: SharedPreferences =
    getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)
  return appPreferences.getInt("id", -1) != -1
}

fun Context.isEnglish(): Boolean {
  val appPreferences: SharedPreferences =
    getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)
  return appPreferences.getString(Constants.LANGUAGE, "").toString() == Constants.DEFAULT_LANGUAGE
}

fun Context.isLoginWithOpenAuth(): Boolean {
  val appPreferences: SharedPreferences =
    getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)
  val isLogin = appPreferences.getInt("id", -1) != -1
  if (!isLogin) startActivity(Intent(this, AuthActivity::class.java))
  return isLogin
}

private val USER_DATA = Pair("USER_DATA", "")
fun View.isChatAllow(): Boolean {
  val appPreferences: SharedPreferences =
    context.getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)

  val value: String? = appPreferences.getString(USER_DATA.first, USER_DATA.second)
  val user = if (value != null && value.isNotEmpty()) Gson().fromJson(value, User::class.java) else User()
  if(user.name == null || user.name.trim().isEmpty()){
    context.showInfo(context.getString(R.string.please_enter_your_name))
    val uri = Uri.Builder()
      .scheme("profile")
      .authority("grand.app.moon.profile")
      .build()
    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
    findNavController().navigate(request)
    return false
  }
  return true
}
