package app.grand.tafwak.presentation.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import app.grand.tafwak.presentation.base.utils.Constants
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.auth.AuthActivity
import app.grand.tafwak.presentation.base.BaseActivity
import app.grand.tafwak.presentation.base.extensions.openActivityAndClearStack
import com.structure.base_mvvm.databinding.ActivitySplashBinding
import app.grand.tafwak.presentation.home.HomeActivity
import app.grand.tafwak.presentation.intro.IntroActivity
import com.zeugmasolutions.localehelper.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

  private val viewModel: SplashViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.activity_splash

  override
  fun setUpViews() {
    binding.viewModel = viewModel
    LocaleHelper.setLocale(this, Locale("ar"))
    decideNavigationLogic()
  }

  private fun decideNavigationLogic() {
//    Handler(Looper.getMainLooper()).postDelayed({
//      val targetActivity = when {
//          viewModel.isFirstTime() -> {
//            IntroActivity::class.java
//          }
//          viewModel.isLogged() -> {
//
//            HomeActivity::class.java
//          }
//          else -> {
//            AuthActivity::class.java
//          }
//      }
//      openActivityAndClearStack(targetActivity)
//    }, 2000)
    viewModel.clickEvent.observeForever {
      val targetActivity = when (it) {
        Constants.FIRST_TIME -> {
          IntroActivity::class.java
        }
        Constants.IS_LOGGED -> {
          HomeActivity::class.java
        }
        else -> {
          AuthActivity::class.java
        }
      }
      openActivityAndClearStack(targetActivity)

    }
  }
}