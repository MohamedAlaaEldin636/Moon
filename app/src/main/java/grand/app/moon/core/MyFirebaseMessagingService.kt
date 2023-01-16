package grand.app.moon.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.common.util.Strings
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import grand.app.moon.R
import com.readystatesoftware.chuck.internal.ui.MainActivity
import grand.app.moon.presentation.base.utils.Constants.TabBarText
import grand.app.moon.presentation.home.HomeActivity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    sendNotification(remoteMessage.data)
  }

  override fun onNewToken(token: String) {

  }

  fun getBitmapFromURL(src: String?): Bitmap? {
    return try {
      val url = URL(src)
      val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
      connection.setDoInput(true)
      connection.connect()
      val input: InputStream = connection.getInputStream()
      BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
      e.printStackTrace()
      null
    }
  } // Author: silentnuke


  private  val TAG = "MyFirebaseMessagingServ"
  private fun sendNotification(messageBody: MutableMap<String, String>) {
    val intent = Intent(this, MainActivity::class.java)

    val pendingIntent = NavDeepLinkBuilder(this)
      .setComponentName(HomeActivity::class.java)
      .setGraph(R.navigation.nav_home)
      .setDestination(R.id.notificationFragment)
      //.setArguments(bundleOf())
      .createPendingIntent()

	  //pendingIntent.fla;
	  // FLAG_IMMUTABLE


    Log.d(TAG, "sendNotification: HRERE")
    messageBody.forEach { (s, s2) ->
      Log.d(TAG, "sendNotification: $s , $s2")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//    val pendingIntent = PendingIntent.getActivity(
//      this, 0 /* Request code */, intent,
//      PendingIntent.FLAG_ONE_SHOT
//    )

    var message = MyApplication.instance.getString(R.string.new_notification)
    if(messageBody["alert"]?.isNotBlank() == true && messageBody["alert"]?.isNotEmpty() == true)
      message = messageBody["alert"].toString()

    else if(messageBody["body"]?.isNotBlank() == true && messageBody["body"]?.isNotEmpty() == true)
      message = messageBody["body"].toString()


    val channelId = "channelId"
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle(messageBody["title"])
      .setContentText(message)
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setContentIntent(pendingIntent)


    if(messageBody["bicon"] != "") {
      val bitmap = getBitmapFromURL(messageBody["bicon"])
      if(bitmap != null)
      notificationBuilder.setStyle(
        NotificationCompat.BigPictureStyle()
          .bigPicture(bitmap)
          .bigLargeIcon(null)
      )
    }

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Since android Oreo notification channel is needed.
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//      val channel = NotificationChannel(
//        channelId,
//        "Channel human readable title",
//        NotificationManager.IMPORTANCE_DEFAULT
//      )
//      notificationManager.createNotificationChannel(channel)
//    }

    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
  }
}