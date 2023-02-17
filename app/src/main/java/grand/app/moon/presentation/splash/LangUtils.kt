@file:Suppress("unused")

package grand.app.moon.presentation.splash

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.zeugmasolutions.localehelper.currentLocale
import grand.app.moon.extensions.orFalse
import java.util.*

fun Application.getApplicationContextMA(): Context {
	val locale = Locale(getCurrentLangFromSharedPrefs())
	Locale.setDefault(locale)

	val configuration = resources.configuration
	configuration.setLocale(locale)
	configuration.setLayoutDirection(locale)

	val displayMetrics = resources.displayMetrics
	resources.updateConfiguration(configuration, displayMetrics)

	return this
}

fun Context.attachBaseContextMA(context: Context?): Context? {
	if (context == null) return null

	val locale = Locale(context.getCurrentLangFromSharedPrefs())
	Locale.setDefault(locale)

	val configuration = context.resources.configuration
	configuration.setLocale(locale)
	configuration.setLayoutDirection(locale)

	val displayMetrics = context.resources.displayMetrics
	context.resources.updateConfiguration(configuration, displayMetrics)

	return context
}

fun Context.onConfigurationChangedMA(newConfig: Configuration): Configuration {
	val locale = Locale(getCurrentLangFromSharedPrefs())
	Locale.setDefault(locale)
	newConfig.setLocale(locale)
	newConfig.setLayoutDirection(locale)
	return newConfig
}

fun Context.getCurrentLangFromSharedPrefs(): String {
	val default = resources.configuration.currentLocale.language

	return getSharedPreferences("abcd", Context.MODE_PRIVATE)?.getString("sakoskao", default) ?: default
}
fun Context.setCurrentLangFromSharedPrefs(lang: String): Boolean {
	return getSharedPreferences("abcd", Context.MODE_PRIVATE)?.edit()?.putString("sakoskao", lang)?.commit().orFalse()
}
