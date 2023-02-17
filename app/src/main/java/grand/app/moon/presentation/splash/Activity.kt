package grand.app.moon.presentation.splash

import android.app.Activity
import android.app.Application
import android.app.Dialog
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
import grand.app.moon.presentation.base.utils.hideLoadingDialog
import grand.app.moon.presentation.base.utils.showLoadingDialog
import java.util.*

/**
 * @return false if will not change since it's same language.
 */
fun Activity.isSameAsCurrentLocale(language: String) = LanguagesHelper.getCurrentLanguage() == language

fun Activity.setCurrentLocale(language: String, recreateActivity: Boolean = true) {
	LanguagesHelper.setLanguage(language)

	if (recreateActivity) {
		recreate()
	}
}

@Suppress("unused")
fun Context.getCurrentLocale(forcedContext: Context? = null): String =
	if (forcedContext == null) LanguagesHelper.getCurrentLanguage() else LanguagesHelper.getCurrentLanguage(forcedContext)

fun Context.getContextForLocaleMA(lang: String, forcedContext: Context? = null): Context {
	val resources = (forcedContext ?: applicationContext).resources

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
		(forcedContext ?: applicationContext).createConfigurationContext(configuration)
	}else {
		resources.updateConfiguration(configuration, displayMetrics)
		(forcedContext ?: applicationContext)
	}

	//LanguagesHelper.setLanguage(lang)

	//recreate()
}

abstract class MABaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

	lateinit var binding: VDB
		private set

	private var progressDialog: Dialog? = null

	fun showLoading() {
		hideLoading()
		kotlin.runCatching {
			progressDialog = showLoadingDialog(this, null)
		}
	}

	fun hideLoading() {
		kotlin.runCatching {
			hideLoadingDialog(progressDialog, this)
		}
	}

	@CallSuper
	override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
		return super.createConfigurationContext(onConfigurationChangedMA(overrideConfiguration))

		//getContextForLocaleMA(LanguagesHelper.getCurrentLanguage())
	}

	@CallSuper
	override fun attachBaseContext(newBase: Context?) {
		super.attachBaseContext(attachBaseContextMA(newBase))
		/*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && newBase != null) {
			super.attachBaseContext(MyContextWrapper.wrap(newBase, getCurrentLocale()))
		}else {
			super.attachBaseContext(newBase)
		}*/
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
