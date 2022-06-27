package grand.app.moon.presentation.base

import android.content.Context
import android.content.IntentSender
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
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
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
//import com.maproductions.mohamedalaa.shared.core.extensions.getLanguage
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import grand.app.moon.appMoonHelper.language.MyContextWrapper
import grand.app.moon.core.MyApplication
import grand.app.moon.helpers.update.ImmediateUpdateActivity
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

  override fun attachBaseContext(newBase: Context?) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && newBase != null) {
      super.attachBaseContext(MyContextWrapper.wrap(newBase, LanguagesHelper.getCurrentLanguage()))
    }else {
      super.attachBaseContext(newBase)
    }
  }

  fun changeLanguage(context: Context,language: String) {

//    conf.setLocale(Locale(getLanguage())

    LanguagesHelper.changeLanguage(context,language)

  }

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
  }



  override
  fun onCreate(savedInstanceState: Bundle?) {
//    LocaleHelper.setLocale(this, Locale("ar"))
    super.onCreate(savedInstanceState)

    initViewBinding()
//    Locale.setDefault(Locale.FRANCE);
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
      setApplicationId("802733460697057")
      sdkInitialize(applicationContext)
//      setAutoLogAppEventsEnabled(true)
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
    LanguagesHelper.changeLanguage(MyApplication.instance, LanguagesHelper.getCurrentLanguage())
    LanguagesHelper.changeLanguage(this, LanguagesHelper.getCurrentLanguage())
    LanguagesHelper.changeLanguage(applicationContext, LanguagesHelper.getCurrentLanguage())

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



  fun updateAuto(immediateUpdateActivity: ImmediateUpdateActivity){
    immediateUpdateActivity.getAppUpdateManager()!!.appUpdateInfo.addOnSuccessListener { it ->
      if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
        try {
          immediateUpdateActivity.getAppUpdateManager()!!
            .startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, 381)
        } catch (e: IntentSender.SendIntentException) {
          e.printStackTrace()
        }
      }
    }
  }

  override
  fun onSupportNavigateUp(): Boolean {
    return navController.value?.navigateUp()!! || super.onSupportNavigateUp()
  }
  override
  fun getDelegate() = localeDelegate.getAppCompatDelegate(super.getDelegate())
}