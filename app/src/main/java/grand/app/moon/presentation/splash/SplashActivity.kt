package grand.app.moon.presentation.splash

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.cometchat.pro.core.CometChat
import com.zeugmasolutions.localehelper.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.databinding.ActivitySplashBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.intro.IntroActivity
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.coroutines.flow.collect
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

  private val viewModel: SplashViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.activity_splash

  private val TAG = "SplashActivity"

  override
  fun setUpViews() {

    viewModel.lang = viewModel.accountRepository.getKeyFromLocal(Constants.LANGUAGE)
    if (viewModel.lang.isEmpty()) {
      viewModel.lang = Constants.DEFAULT_LANGUAGE
      viewModel.accountRepository.saveKeyToLocal(Constants.LANGUAGE, viewModel.lang)
    }
    Log.d(TAG, "setUpViews: ${viewModel.lang}")
    binding.viewModel = viewModel
    viewModel.home()
    decideNavigationLogic()
  }



  private fun decideNavigationLogic() {
    viewModel.clickEvent.observe(this, {
      Log.d(TAG, "decideNavigationLogic: $it")
      val targetActivity = when (it) {
        Constants.FIRST_TIME -> {
          Log.d(TAG, "FIRST_TIME: ")
          IntroActivity::class.java
        }
        else -> {
          Log.d(TAG, "HomeActivity: ")
          HomeActivity::class.java
        }
      }
//      updateLocale(viewModel.lang)
      openActivityAndClearStack(targetActivity)
    })

    lifecycleScope.launchWhenResumed {
      viewModel.categoryItemResponse.collect {
        Log.d(TAG, "decideNavigationLogic: ")
        when (it) {
          is Resource.Success -> {
            viewModel.saveCategories(it.value)
            viewModel.isCategories = true
            viewModel.redirect()
          }
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel._countriesResponse.collect {
        when (it) {
          is Resource.Success -> {
            viewModel.saveCountries(it.value)
            viewModel.isCountries = true
            viewModel.redirect()
          }
        }
      }
    }
  }
}