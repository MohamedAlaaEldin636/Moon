package grand.app.moon.helpers.update

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import java.lang.Exception

class ImmediateUpdateActivity {


  private val TAG = "ImmediateUpdateActivity"
  private var activity: Activity? = null
  private var appUpdateManager: AppUpdateManager? = null

  constructor(activity: Activity){
    this.activity = activity
    init()
  }

  private fun init() {
    activity?.let { activity ->
      appUpdateManager = AppUpdateManagerFactory.create(activity)
      val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.getAppUpdateInfo()
      Log.d(TAG, appUpdateInfoTask.toString())
      appUpdateInfoTask.addOnSuccessListener { it ->
        if (it.isUpdateTypeAllowed(
            AppUpdateType.IMMEDIATE
          )
        ) {
          try {
            appUpdateManager?.startUpdateFlowForResult(
              it, AppUpdateType.IMMEDIATE, activity, 381
            )
          } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
            Log.e(TAG, "init: " + e.message)
          }
        }
      }.addOnFailureListener {
        it.printStackTrace()
      }
    }

  }

  fun getAppUpdateManager(): AppUpdateManager? {
    return appUpdateManager
  }
}