package grand.app.moon.core

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.onesignal.OneSignal
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import dagger.hilt.android.HiltAndroidApp
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

fun Context?.getMyApplication() = this?.applicationContext as? MyApplication

fun Context?.makeAppInitializations() {
	getMyApplication()?.performAllInitializations()
}

@HiltAndroidApp
class MyApplication : Application() {

	var checkedAppGlobalAnnouncement = false
	var showedAppGlobalAnnouncement = false

	private val supervisorJob = SupervisorJob()
	val applicationScope = CoroutineScope(Dispatchers.IO + supervisorJob)

  override
  fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
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
			//initStoryViewer() //todo this makes a problem for now restarting activity isa.

			// Logging set to help debug issues, remove before releasing your app.
			//OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
			// OneSignal Initialization
			OneSignal.initWithContext(this@MyApplication)
			OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)
		}
	}

  private fun initStoryViewer() {
    val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024)
    val databaseProvider: DatabaseProvider = ExoDatabaseProvider(this)

    if (simpleCache == null) {
      simpleCache = SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, databaseProvider)
    }
  }

  companion object {
    var simpleCache: SimpleCache? = null

	  lateinit var instance : MyApplication
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