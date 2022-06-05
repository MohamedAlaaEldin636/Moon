package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.onesignal.OneSignal
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ThirdPartyHelper
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.logoutCometChat
import grand.app.moon.core.extenstions.showError
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.splash.SplashActivity
import kotlinx.coroutines.runBlocking

fun Context.dpToPx(value: Float): Float {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
  )
}

/** - Layout inflater from `receiver`, by using [LayoutInflater.from] */
val Context.layoutInflater: LayoutInflater
  get() = LayoutInflater.from(this)

/**
 * - Inflates a layout from [layoutRes] isa.
 *
 * @param parent provide [ViewGroup.LayoutParams] to the returned root view, default is `null`
 * @param attachToRoot if true then the returned view will be attached to [parent] if not `null`,
 * default is false isa.
 *
 * @return rootView in the provided [layoutRes] isa.
 */
@JvmOverloads
fun Context.inflateLayout(
  @LayoutRes layoutRes: Int,
  parent: ViewGroup? = null,
  attachToRoot: Boolean = false
): View {
  return layoutInflater.inflate(layoutRes, parent, attachToRoot)
}

fun Context.logout(){
  OneSignal.removeExternalUserId();
  ThirdPartyHelper.clearOpenSignal()
  MyApplication.instance.logoutCometChat()
  AppPreferences(MyApplication.instance).clearUser()
}

fun Context.loginPage(){
  Handler(Looper.getMainLooper()).postDelayed({
    MyApplication.instance.baseContext.showError(MyApplication.instance.baseContext.getString(R.string.please_login_agin))
  }, 100)
  Intent(this, SplashActivity::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(this)
  }
}

fun Context.checkSelfPermissionGranted(permission: String): Boolean {
  return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
