package com.structure.base_mvvm.presentation.splash

import androidx.activity.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.auth.AuthActivity
import com.structure.base_mvvm.presentation.base.BaseActivity
import com.structure.base_mvvm.presentation.base.extensions.openActivityAndClearStack
import com.structure.base_mvvm.presentation.databinding.ActivitySplashBinding
import com.structure.base_mvvm.presentation.home.HomeActivity
import com.structure.base_mvvm.presentation.intro.IntroActivity
import com.zeugmasolutions.localehelper.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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