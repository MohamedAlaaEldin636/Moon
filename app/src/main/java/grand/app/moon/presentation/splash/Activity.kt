package grand.app.moon.presentation.splash

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import grand.app.moon.appMoonHelper.language.MyContextWrapper
import java.util.*

/**
 * @return false if will not change since it's same language.
 */
fun Activity.isSameAsCurrentLocale(language: String) = LanguagesHelper.getCurrentLanguage() == language

fun Activity.setCurrentLocale(language: String) {
	LanguagesHelper.setLanguage(language)

	recreate()
}

@Suppress("unused")
fun Activity.getCurrentLocale(): String = LanguagesHelper.getCurrentLanguage()

fun Activity.getContextForLocaleMA(lang: String): Context {
	val resources = applicationContext.resources

	/*if (lang == Locale.getDefault().language && lang == resources.configuration.currentLocale.language) {
		return
	}*/

	val locale = Locale(lang)
	Locale.setDefault(locale)

	val configuration = resources.configuration
	configuration.setLocale(locale)
	configuration.setLayoutDirection(locale)

	val displayMetrics = resources.displayMetrics
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		applicationContext.createConfigurationContext(configuration)
	}else {
		resources.updateConfiguration(configuration, displayMetrics)
		applicationContext
	}

	//LanguagesHelper.setLanguage(lang)

	//recreate()
}

abstract class MABaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

	lateinit var binding: VDB
		private set

	@CallSuper
	override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
		return getContextForLocaleMA(LanguagesHelper.getCurrentLanguage()) // "ar"
	}

	@CallSuper
	override fun attachBaseContext(newBase: Context?) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && newBase != null) {
			super.attachBaseContext(MyContextWrapper.wrap(newBase, getCurrentLocale()))
		}else {
			super.attachBaseContext(newBase)
		}
	}

	@CallSuper
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, getLayoutId())
		binding.lifecycleOwner = this
	}

	@LayoutRes
	abstract fun getLayoutId(): Int

}
