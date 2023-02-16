package grand.app.moon.presentation.splash

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zeugmasolutions.localehelper.currentLocale
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import java.util.*

fun AppCompatActivity.setLocaleMA(lang: String) {
	val resources = applicationContext.resources

	if (lang == Locale.getDefault().language && lang == resources.configuration.currentLocale.language) {
		return
	}

	val locale = Locale(lang)
	Locale.setDefault(locale)

	val configuration = resources.configuration
	configuration.setLocale(locale)
	configuration.setLayoutDirection(locale)

	val displayMetrics = resources.displayMetrics
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		createConfigurationContext(configuration)
	}else {
		resources.updateConfiguration(configuration, displayMetrics)
	}

	LanguagesHelper.setLanguage(lang)

	recreate()
}

abstract class MABaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

	lateinit var binding: VDB
		private set

	@CallSuper
	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)

		binding = DataBindingUtil.setContentView(this, getLayoutId())
		binding.lifecycleOwner = this
	}

	@LayoutRes
	abstract fun getLayoutId(): Int

}
