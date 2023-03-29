package grand.app.moon.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.multidex.MultiDex
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import grand.app.moon.core.extenstions.InitialAppLaunch
import grand.app.moon.core.extenstions.getInitialAppLaunch
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.indexOfFirstOrNull
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome
import grand.app.moon.presentation.splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import javax.net.ssl.SSLContext

fun Context?.getMyApplication() = this?.applicationContext as? MyApplication

fun Context?.makeAppInitializations() {
	getMyApplication()?.performAllInitializations()
}

@HiltAndroidApp
class MyApplication : Application() {

	companion object {
		lateinit var instance : MyApplication

		private const val DEFAULT_LANGUAGE = "ar"

		var deepLinkUri: Uri? = null
		var usedDeepLink = false
	}

	@Inject
	lateinit var loginUseCase: LogInUseCase

	fun logOutAsync() {
		applicationScope.launch {
			loginUseCase.logout()
		}
	}

	var checkedAppGlobalAnnouncement = false
	var showedAppGlobalAnnouncement = false

	private val supervisorJob = SupervisorJob()
	val applicationScope = CoroutineScope(Dispatchers.IO + supervisorJob)

	private val newFollowingStateChangeStores = mutableListOf<ItemStoreInResponseHome>()
	private val newFavStateChangeAds = mutableSetOf<ItemAdvertisementInResponseHome>()

	fun getStoresFollowingStateChanges(): List<ItemStoreInResponseHome> {
		return newFollowingStateChangeStores.toList()
	}
	fun getStoresFollowingStateChangesOnceTillNewChangesCome(): List<ItemStoreInResponseHome> {
		val list = newFollowingStateChangeStores.toList()
		newFollowingStateChangeStores.clear()
		return list
	}
	/** @param item after follow state change */
	@Synchronized
	fun followOrUnFollowStoreFromNotHomeScreen(item: ItemStoreInResponseHome) {
		val index = newFollowingStateChangeStores.indexOfFirstOrNull { it.id == item.id }
		if (index == null) newFollowingStateChangeStores.add(item) else {
			newFollowingStateChangeStores[index] = item
		}
	}

	// todo
	/*fun getAdsFavStateChanges(): List<ItemAdvertisementInResponseHome> {
		return newFavStateChangeAds.toList()
	}
	fun getAdsFavStateChangesOnceTillNewChangesCome(): List<ItemAdvertisementInResponseHome> {
		val list = newFollowingStateChangeStores.toList()
		newFollowingStateChangeStores.clear()
		return list
	}
	*//** @param item after follow state change *//*
	@Synchronized
	fun favOrUnFavAdvFromNotHomeScreen(item: ItemAdvertisementInResponseHome) {
		val index = newFollowingStateChangeStores.indexOfFirstOrNull { it.id == item.id }
		if (index == null) newFollowingStateChangeStores.add(item) else {
			newFollowingStateChangeStores[index] = item
		}
	}*/

	override fun getApplicationContext(): Context {
		return getApplicationContextMA(/*getCurrentLocale(this)*/)//getContextForLocaleMA("ar", this)
	}

	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(onConfigurationChangedMA(newConfig))
	}

	override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
		return super.createConfigurationContext(onConfigurationChangedMA(overrideConfiguration))
	}

	override fun attachBaseContext(base: Context?) {
		base?.apply {
			if (getInitialAppLaunch() == InitialAppLaunch.SHOW_WELCOMING_SCREENS) {
				setCurrentLangFromSharedPrefs(DEFAULT_LANGUAGE)

				val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(DEFAULT_LANGUAGE)
				AppCompatDelegate.setApplicationLocales(appLocale)
			}
		}

		super.attachBaseContext(attachBaseContextMA(base))

		MultiDex.install(this)
	}

  override
  fun onCreate() {
    super.onCreate()
	  //initChat()
    //updateAndroidSecurityProvider()
	  //initStoryViewer()
    instance = this

    // Logging set to help debug issues, remove before releasing your app.
	  //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
    // OneSignal Initialization
	  //OneSignal.initWithContext(this)
	  //OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)

	  //performAllInitializations()
  }

	fun performAllInitializations() {
		applicationScope.launch {
			initChat()
			updateAndroidSecurityProvider()

			// Logging set to help debug issues, remove before releasing your app.
			//OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
			// OneSignal Initialization
			OneSignal.initWithContext(this@MyApplication)
			OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)
			//OneSignal.promptForPushNotifications()

			// Below is secret key of IOS I guess wait to get from Alaa for android
			MyLogger.e("app id $packageName which should be grand.app.moon")

			GoSellSDKUtils.beforeAnyLaunchSetups(this@MyApplication)
		}
	}

  private fun initChat() {
    val appID = Constants.CHAT_APP_ID
    val region = Constants.CHAT_REGION
    val appSettings = AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build()

    CometChat.init(this, appID, appSettings, object : CometChat.CallbackListener<String>() {
      override fun onSuccess(successMessage: String) {
      }

      override fun onError(p0: CometChatException?) {
//        TODO("Not yet implemented")
      }
    })
  }

  private fun updateAndroidSecurityProvider() {
    // To fix the following issue, when run app in cellular data, Apis not working
    // javax.net.ssl.SSLHandshakeException: SSL handshake aborted: ssl=0x7edfc49e08: I/O error during system call, Connection reset by peer
    try {
      ProviderInstaller.installIfNeeded(applicationContext)
      val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
      sslContext.init(null, null, null)
      sslContext.createSSLEngine()
    } catch (e: GooglePlayServicesRepairableException) {
      e.printStackTrace()
    } catch (e: GooglePlayServicesNotAvailableException) {
      e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    } catch (e: KeyManagementException) {
      e.printStackTrace()
    }
  }
}