package grand.app.moon.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.InitialAppLaunch
import grand.app.moon.core.extenstions.getInitialAppLaunch
import grand.app.moon.core.makeAppInitializations
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ActivityMaSplash2Binding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.intro.IntroActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// done problem of launching application white screen but still takes time and to fix it will
// requires a lot of debugging which will require days or weeks of it and delay done after showing ui
/**
 * 1. load app initializations as a lazy initialization from here as here is always the first
 * entry point
 * 2. load in background in app scope the categories and save them to cache them isa.
 * 3. check app state and decide next screen which will either be intro activity or home one.
 */
@AndroidEntryPoint
class MASplash2Activity : AppCompatActivity() {

	@Inject
	lateinit var repoShop: RepoShop

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val binding = DataBindingUtil.setContentView<ActivityMaSplash2Binding>(this, R.layout.activity_ma_splash_2)

		MyLogger.e("heeeeeeeeeeeey ${intent.getStringExtra(NotificationsUtils.INTENT_EXTRA_KEY_MODEL_AS_JSON)}")

		val appLinkData = intent?.data
		MyApplication.deepLinkUri = appLinkData
		MyApplication.usedDeepLink = appLinkData == null

		binding.splashImageView.setupWithGlide {
			load(R.drawable.aaaa).saveDiskCacheStrategyAll()
		}

		binding.root.post {
			lifecycleScope.launch {
				delay(250)

				makeAppInitializations()

				delay(50)

				applicationScope?.launch {
					if (getInitialAppLaunch() == InitialAppLaunch.SHOW_HOME) {
						repoShop.fetchAnnouncementAndSaveItLocally()
					}

					repoShop.fetchAllCategoriesAndSaveThemLocallyIfPossible()
				}

				val jClass = when (getInitialAppLaunch()) {
					InitialAppLaunch.SHOW_WELCOMING_SCREENS -> IntroActivity::class.java
					InitialAppLaunch.SHOW_HOME -> HomeActivity::class.java
				}

				openActivityAndClearStack(jClass)
			}
		}
	}

}
