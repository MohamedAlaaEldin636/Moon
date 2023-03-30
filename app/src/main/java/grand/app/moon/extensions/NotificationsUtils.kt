package grand.app.moon.extensions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.IconCompat
import com.onesignal.OSNotification
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.presentation.splash.MASplash2Activity
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

object NotificationsUtils {

	const val INTENT_EXTRA_KEY_MODEL_AS_JSON = "NotificationsUtils.INTENT_EXTRA_KEY_MODEL_AS_JSON"

	private const val CHANNEL_ID_ADMIN = "CHANNEL_ID_ADMIN"
	private const val NOTIFICATION_ID_ADMIN = 41

	private const val CHANNEL_ID_STORE_ADDED_ADVERTISEMENT = "CHANNEL_ID_STORE_ADDED_ADVERTISEMENT"
	private const val NOTIFICATION_ID_STORE_ADDED_ADVERTISEMENT = 42

	private const val CHANNEL_ID_COMET_CHAT = "CHANNEL_ID_COMET_CHAT"
	private const val NOTIFICATION_ID_COMET_CHAT = 43

	const val BROADCAST_INTENT_KEY_INCREMENT_NOTIFICATIONS_COUNT = "BROADCAST_INTENT_KEY_INCREMENT_NOTIFICATIONS_COUNT"

	@JvmStatic
	fun showNotificationAndSendBroadcast(
		applicationContext: Context,
		data: OSNotification
	) {
		val model = Model.fromNotificationMappedData(data)

		MyLogger.e("Pre Showing Notification $data")

		val pendingIntent = getNotificationPendingIntent(applicationContext, model)

		val (channelId, channelName, generalNotificationId) = when (model.type) {
			Type.ADMIN -> Triple(
				CHANNEL_ID_ADMIN,
				applicationContext.getString(R.string.app_name),
				NOTIFICATION_ID_ADMIN
			)
			Type.STORE_ADDED_ADVERTISEMENT -> Triple(
				CHANNEL_ID_STORE_ADDED_ADVERTISEMENT,
				applicationContext.getString(R.string.notif_channel_store_added_adv),
				NOTIFICATION_ID_STORE_ADDED_ADVERTISEMENT
			)
			Type.COMET_CHAT -> Triple(
				CHANNEL_ID_COMET_CHAT,
				applicationContext.getString(R.string.notif_channel_comet_chat),
				NOTIFICATION_ID_COMET_CHAT
			)
		}

		// Example in raw android directory -> ringing.wav -> see HassanU project for example isa.
		val soundUri: Uri? = null/*if (sound.isNullOrEmpty() || sound == "default") null else {
			val indexOfDot = sound.indexOf(".")

			val resSound: Int = appContext.resources.getIdentifier(
				if (indexOfDot == -1) sound else sound.substring(0, indexOfDot).apply {
					Timber.e("NotificationsService -> string $this")
				},
				"raw",
				appContext.packageName
			)

			Timber.e("NotificationsService -> resSound $resSound")

			if (resSound == 0) null else Uri.Builder()
				.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
				.authority(appContext.packageName)
				.appendPath(resSound.toString())
				.build()
		}*/

		showNotificationToLaunchPendingIntent(
			pendingIntent,
			applicationContext,
			model,
			channelId,
			channelName,
			model.notificationId/*if (model.notificationId <= 0) generalNotificationId else model.notificationId*/,
			soundUri
		)

		// Send broadcast
		applicationContext.sendBroadcast(Intent(BROADCAST_INTENT_KEY_INCREMENT_NOTIFICATIONS_COUNT))
	}

	private fun showNotificationToLaunchPendingIntent(
		pendingIntent: PendingIntent,
		applicationContext: Context,
		model: Model,
		channelId: String,
		channelName: String,
		notificationId: Int,
		soundUri: Uri?,
	) {
		// 3 ideas 1 run foreground service like whatsapp checkking new notifications untill laoading
		/*
		image as bitmap
		2. OR herre run in bg thread and update notifiaction although not guranteed to be killed
		3. OR whenever u follow a store save it's image but this is not a good approach cuz in case changed image isa.
		 */

		MyLogger.e("Pre Showing Notification $model")

		val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
		val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
			.setPriority(NotificationCompat.PRIORITY_MAX)
			.let {
				if (model.storeImgOfAddedAdv.isNullOrEmpty()) it.setSmallIcon(R.mipmap.ic_launcher) else {
					val bitmap = runBlocking {
						applicationContext.getBitmapFromURLUsingGlide(
							model.storeImgOfAddedAdv,
							applicationContext.dpToPx(24f).roundToInt()
						)
					}

					if (bitmap != null) {
						it.setSmallIcon(IconCompat.createWithBitmap(bitmap))
					}else {
						it.setSmallIcon(R.mipmap.ic_launcher)
					}
				}
			}
			.setOnlyAlertOnce(false)
			.setContentTitle(model.title)
			.setContentText(model.msg)
			.let {
				val bitmap = if (model.image.isNullOrEmpty()) null else runBlocking {
					applicationContext.getBitmapFromURLUsingGlide(
						model.image,
					)
				}
				if (bitmap != null) {
					it.setLargeIcon(bitmap)
						.setStyle(
							NotificationCompat.BigPictureStyle()
								.bigPicture(bitmap)
								.bigLargeIcon(null)
						)
				}else {
					it.setStyle(NotificationCompat.BigTextStyle().bigText(model.msg))
				}
			}
			.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
			.setAutoCancel(true)
			.setShowWhen(true)
			.setDefaults(/*Notification.DEFAULT_SOUND or */Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
			//, /*AudioManager.*//*STREAM_NOTIFICATION*/
			.setSound(soundUri ?: defaultSoundUri, AudioManager.STREAM_NOTIFICATION)
			//.setVibrate()
			.setContentIntent(pendingIntent)

		val notificationManager = applicationContext.getSystemService<NotificationManager>() ?: return

		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelId,
				channelName,
				NotificationManager.IMPORTANCE_HIGH
			)
			val audioAttrs = AudioAttributes.Builder()
				/*
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
		.setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
				 */
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.setUsage(AudioAttributes.USAGE_NOTIFICATION)
				.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
				.build()
			channel.setSound(soundUri ?: defaultSoundUri, audioAttrs)
			notificationBuilder.setChannelId(channelId)
			notificationManager.createNotificationChannel(channel)
		}

		notificationManager.notify(notificationId, notificationBuilder.build())
	}

	private fun getNotificationPendingIntent(
		applicationContext: Context,
		model: Model
	): PendingIntent {
		return getActivityPendingIntent(
			applicationContext,
			MASplash2Activity::class.java
		) {
			putExtra(INTENT_EXTRA_KEY_MODEL_AS_JSON, model.toJsonInlinedOrNull())
		}
	}

	private fun getActivityPendingIntent(
		applicationContext: Context,
		jClass: Class<*>,
		adjustments: Intent.() -> Unit = {}
	): PendingIntent {
		val intent = Intent(applicationContext, jClass)
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
		intent.adjustments()

		return PendingIntent.getActivity(
			applicationContext,
			0 /* Request code */,
			intent,
			PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		)
	}

	data class Model(
		val title: String?,
		val msg: String?,
		val image: String?,
		val type: Type,
		val advId: Int?,
		val storeImgOfAddedAdv: String?,
		val userChatId: Int?,
		val notificationId: Int,
	) {
		companion object {

			fun fromNotificationMappedData(data: OSNotification): Model {
				val advId = data.additionalData?.optInt("advertisement_id").let {
					if (it == 0) null else it
				}
				val storeImageOfAddedAdvertisement = data.additionalData?.optString("store_image_of_added_advertisement").let {
					if (it.isNullOrEmpty()) null else it
				}
				val type = when {
					advId != null -> Type.STORE_ADDED_ADVERTISEMENT
					//advId != null -> Type.COMET_CHAT // todo
					else -> Type.ADMIN
				}

				MyLogger.e("data.androidNotificationId ${data.androidNotificationId}")

				return Model(
					data.title,
					data.body,
					if (type == Type.STORE_ADDED_ADVERTISEMENT) null else data.bigPicture,
					type,
					advId,
					storeImageOfAddedAdvertisement,
					null, // userChatId = todo,
					data.androidNotificationId
				)
			}

		}
	}

	enum class Type {
		ADMIN,
		STORE_ADDED_ADVERTISEMENT,
		COMET_CHAT
	}

}
