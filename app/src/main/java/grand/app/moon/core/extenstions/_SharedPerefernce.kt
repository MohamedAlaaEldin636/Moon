package grand.app.moon.core.extenstions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import com.google.gson.Gson
import grand.app.moon.R
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.fromJsonInlinedOrNull
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.toJsonInlinedOrNull
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.utils.Constants

private const val SHARED_PREFS_CATEGORIES_WITH_SUB_CATEGORIES_AND_BRANDS = "SHARED_PREFS_CATEGORIES_WITH_SUB_CATEGORIES_AND_BRANDS"
private const val SHARED_PREFS_SELECTED_LANG_BEFORE = "SHARED_PREFS_SELECTED_LANG_BEFORE"

fun Context.getSelectedLangBeforeThenReset(): Boolean {
	val selectedBefore =  getDefAppPrefs()?.getBoolean(
		SHARED_PREFS_SELECTED_LANG_BEFORE,
		false
	).orFalse()

	setSelectedLangBefore(false)

	return selectedBefore
}
fun Context.setSelectedLangBefore(selectedBefore: Boolean = true) = getDefAppPrefs()?.edit()?.putBoolean(
	SHARED_PREFS_SELECTED_LANG_BEFORE,
	selectedBefore
)?.commit().orFalse()

enum class InitialAppLaunch {
	SHOW_WELCOMING_SCREENS,
	SHOW_HOME
}

fun Context.setCategoriesWithSubCategoriesAndBrands(
	value: List<ItemCategory>?
) = getDefAppPrefs().setUsingJson(value.orEmpty(), SHARED_PREFS_CATEGORIES_WITH_SUB_CATEGORIES_AND_BRANDS)
fun Context.getCategoriesWithSubCategoriesAndBrands(
	defValue: List<ItemCategory> = emptyList()
) = getDefAppPrefs().getUsingJson(defValue, SHARED_PREFS_CATEGORIES_WITH_SUB_CATEGORIES_AND_BRANDS)

private fun Context.getDefAppPrefs() =
	getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)

private inline fun <reified T> SharedPreferences?.getUsingJson(
	defValue: T,
	key: String = T::class.java.name
): T {
	return this?.getString(
		key,
		""
	).fromJsonInlinedOrNull<T>() ?: defValue
}
private inline fun <reified T> SharedPreferences?.setUsingJson(
	value: T,
	key: String = T::class.java.name
): Boolean {
	return this?.edit()
		?.putString(key, value.toJsonInlinedOrNull().orEmpty())
		?.commit().orFalse()
}

fun Context.setInitialAppLaunch(initialAppLaunch: InitialAppLaunch): Boolean =
	getDefAppPrefs().setUsingJson(initialAppLaunch)

fun Context.getInitialAppLaunch(): InitialAppLaunch =
	getDefAppPrefs().getUsingJson(InitialAppLaunch.SHOW_WELCOMING_SCREENS)

/**
 * @return `true` if not logged in and made redirection isa.
 */
fun Context.redirectIfNotLoggedIn(): Boolean {
	return if (isLogin()) false else {
		startActivity(Intent(this, AuthActivity::class.java))
		true
	}
}

fun Context.isLogin(): Boolean {
  val appPreferences: SharedPreferences =
    getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)
  return appPreferences.getInt("id", -1) != -1
}

fun Context.getUserIdOrNull(): Int? {
  val appPreferences: SharedPreferences =
    getSharedPreferences(AppPreferences.APP_PREFERENCES_NAME, AppPreferences.MODE)
  return appPreferences.getInt("id", -1).let { if (it == -1) null else it }
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
