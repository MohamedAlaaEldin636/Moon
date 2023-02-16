package grand.app.moon.presentation.splash

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.zeugmasolutions.localehelper.currentLocale
import grand.app.moon.R
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import grand.app.moon.core.makeAppInitializations
import grand.app.moon.databinding.ActivityMaSplash2Binding
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.asVideo
import grand.app.moon.extensions.saveDiskCacheStrategyAll
import grand.app.moon.extensions.setupWithGlide
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.utils.showMessage
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.intro.IntroActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

// done problem of launching application white screen but still takes time and to fix it will
// requires a lot of debugging which will require days or weeks of it and delay done after showing ui
class MASplash2Activity : AppCompatActivity() {

	private lateinit var binding: ActivityMaSplash2Binding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.activity_ma_splash_2)
		binding.lifecycleOwner = this

		binding.splashImageView.setupWithGlide {
			load(R.drawable.aaaa).saveDiskCacheStrategyAll()
		}

		binding.root.post {
			lifecycleScope.launch {
				delay(500)

				makeAppInitializations()

				setLocaleMA("en")

				delay(500)

				MyLogger.e("udhiewudhweiudwidh -1woqkpodkpoasd")

				openActivityAndClearStack(IntroActivity::class.java)
			}
		}
	}

	fun setLocale(lang: String) {
		val res = resources
		val config = Configuration(res.configuration)
		config.setLocale(Locale(lang))
		res.updateConfiguration(config, res.displayMetrics)
		recreate()
	}

	private fun setLocale2(lang: String) {
		val resources = applicationContext.resources

		MyLogger.e("langssssss -> $lang === ${Locale.getDefault().language} === ${resources.configuration.currentLocale.language}")
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

}
