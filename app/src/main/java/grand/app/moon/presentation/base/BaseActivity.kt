package grand.app.moon.presentation.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.facebook.FacebookSdk.fullyInitialize
import com.facebook.FacebookSdk.isInitialized
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled
import com.facebook.FacebookSdk.setApplicationId
import com.facebook.FacebookSdk.setAutoInitEnabled
import com.facebook.FacebookSdk.setAutoLogAppEventsEnabled
import com.google.firebase.auth.FirebaseAuth
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import java.util.Locale

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
  private val localeDelegate: LocaleHelperActivityDelegate = LocaleHelperActivityDelegateImpl()
  private var _binding: VB? = null
  open val binding get() = _binding!!
   lateinit var navController: LiveData<NavController>

  override
  fun createConfigurationContext(overrideConfiguration: Configuration): Context {
    val context = super.createConfigurationContext(overrideConfiguration)
    return LocaleHelper.onAttach(context)
  }

  override
  fun getApplicationContext(): Context =
    localeDelegate.getApplicationContext(super.getApplicationContext())

  override
  fun onResume() {
    super.onResume()
    localeDelegate.onResumed(this)
  }

  override
  fun onPause() {
    super.onPause()
    localeDelegate.onPaused()
  }

  override
  fun onCreate(savedInstanceState: Bundle?) {
//    LocaleHelper.setLocale(this, Locale("ar"))
    super.onCreate(savedInstanceState)
    initViewBinding()
    setContentView(binding.root)

    if (savedInstanceState == null) {
      setUpBottomNavigation()
      setUpNavigationDrawer()
    }
    initFacebook()
    setUpViews()
  }

  private fun initFacebook() {
    if (!isInitialized()) {
      setApplicationId("716648052821423")
      sdkInitialize(applicationContext)
      setAutoLogAppEventsEnabled(true)
      fullyInitialize()
      setAutoInitEnabled(true)
      setAdvertiserIDCollectionEnabled(true)
    }
  }


  override
  fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    setUpBottomNavigation()
    setUpNavigationDrawer()
  }

  private fun initViewBinding() {
    _binding = DataBindingUtil.setContentView(this, getLayoutId())
    binding.lifecycleOwner = this
    binding.executePendingBindings()
  }

  @LayoutRes
  abstract fun getLayoutId(): Int

  open fun setUpBottomNavigation() {}
  open fun setUpNavigationDrawer() {}

  open fun setUpViews() {}

  open fun updateLocale(language: String) {
    localeDelegate.setLocale(this, Locale(language))
  }

  override
  fun onSupportNavigateUp(): Boolean {
    return navController.value?.navigateUp()!! || super.onSupportNavigateUp()
  }

  override
  fun getDelegate() = localeDelegate.getAppCompatDelegate(super.getDelegate())
}