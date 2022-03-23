package grand.app.moon.presentation.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.databinding.ActivitySplashBinding
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.intro.IntroActivity
import com.zeugmasolutions.localehelper.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.flow.collect
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

  private val viewModel: SplashViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.activity_splash

  override
  fun setUpViews() {
    viewModel.lang = viewModel.accountRepository.getKeyFromLocal(Constants.LANGUAGE)
    if(viewModel.lang.isEmpty()){
      viewModel.lang = "ar"
      LocaleHelper.setLocale(this, Locale(viewModel.lang))
    }
    viewModel.accountRepository.saveKeyToLocal(Constants.LANGUAGE,viewModel.lang)
    binding.viewModel = viewModel
    viewModel.home()
    decideNavigationLogic()
  }

  private fun decideNavigationLogic() {
    viewModel.clickEvent.observe(this, {
      val targetActivity = when (it) {
        Constants.FIRST_TIME -> {
          IntroActivity::class.java
        }
        else -> {
          HomeActivity::class.java
        }
      }
      updateLocale(viewModel.lang)
      openActivityAndClearStack(targetActivity)
    })

    lifecycleScope.launchWhenResumed {
      viewModel.categoryItemResponse.collect {
        when (it) {

          is Resource.Success -> {
            viewModel.saveCategories(it.value)
            viewModel.redirect()
          }

        }
      }
    }
  }
}