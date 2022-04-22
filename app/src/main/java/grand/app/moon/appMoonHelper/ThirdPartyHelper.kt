package grand.app.moon.appMoonHelper

import com.onesignal.OneSignal

object ThirdPartyHelper {
  fun clearOpenSignal(){
    OneSignal.removeExternalUserId();
  }
}