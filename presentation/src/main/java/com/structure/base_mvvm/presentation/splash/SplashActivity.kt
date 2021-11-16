package com.structure.base_mvvm.presentation.splash

import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
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
    LocaleHelper.setLocale(this, Locale("ar"))
    decideNavigationLogic()
  }

  private fun decideNavigationLogic() {
    Handler(Looper.getMainLooper()).postDelayed({
      val targetActivity = if (viewModel.isFirstTime()) {
        IntroActivity::class.java
      } else {
        HomeActivity::class.java
      }
      openActivityAndClearStack(targetActivity)
    }, 2000)
  }
}