package grand.app.moon.core.extenstions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.onesignal.OneSignal
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ThirdPartyHelper
import grand.app.moon.core.MyApplication
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.splash.MASplash2Activity
import grand.app.moon.presentation.splash.SplashActivity

fun Context.dpToPx(value: Float): Float {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
  )
}

fun Context.spToPx(value: Float): Float {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics
  )
}

fun Context.pxToSp(value: Float): Float {
	return value / resources.displayMetrics.scaledDensity
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

fun Context.convertToString(model: Store) : String{
  val gson = Gson()
  return gson.toJson(model)
}

fun Context.convertToString(model: ArrayList<Explore>) : String{
  val gson = Gson()
  return gson.toJson(model)
}

fun Context.convertToStringArray(model: ArrayList<String>) : String{
  val gson = Gson()
  return gson.toJson(model)
}

fun Context.convertToString(model: UpdateProfileRequest) : String{
  val gson = Gson()
  return gson.toJson(model)
}


fun Context.logout(){
  OneSignal.removeExternalUserId();
  ThirdPartyHelper.clearOpenSignal()
  MyApplication.instance.logoutCometChat()
  AppPreferences(MyApplication.instance).clearUser()
}

fun Context.loginPage(){
  Handler(Looper.getMainLooper()).postDelayed({
    MyApplication.instance.baseContext.showInfo(MyApplication.instance.baseContext.getString(R.string.please_login_agin))
  }, 100)
  Intent(this, MASplash2Activity::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(this)
  }
}

fun Context.checkSelfPermissionGranted(permission: String): Boolean {
  return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkSelfPermissionsGranted(vararg permissions: String): Boolean {
  return permissions.all { permission ->
	  ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
  }
}
