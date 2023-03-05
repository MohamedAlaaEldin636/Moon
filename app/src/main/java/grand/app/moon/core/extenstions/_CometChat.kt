package grand.app.moon.core.extenstions

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import com.cometchat.pro.uikit.ui_settings.UIKitSettings
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import grand.app.moon.presentation.base.utils.Constants

private val TAG = "_CometChat"
fun Context.startChatPage(v: View, user: User) {
  v.enable()
  Log.d(TAG, "startChatPage: WORKED")
  val intent = Intent(v.context, CometChatMessageListActivity::class.java)
  intent.putExtra(UIKitConstants.IntentStrings.UID, user.uid)
  intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.avatar)
  intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.status)
  intent.putExtra(UIKitConstants.IntentStrings.NAME, user.name)
  intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER)
  intent.putExtra(UIKitConstants.IntentStrings.LINK, user.link)
  intent.putExtra(UIKitSettings.LANGUAGE_DATA, LanguagesHelper.getCurrentLanguage())

  v.context.startActivity(intent)
}
fun Context.startChatPage2(user: User) {
  val intent = Intent(this, CometChatMessageListActivity::class.java)
  intent.putExtra(UIKitConstants.IntentStrings.UID, user.uid)
  intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.avatar)
  intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.status)
  intent.putExtra(UIKitConstants.IntentStrings.NAME, user.name)
  intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER)
  intent.putExtra(UIKitConstants.IntentStrings.LINK, user.link)
  intent.putExtra(UIKitSettings.LANGUAGE_DATA, LanguagesHelper.getCurrentLanguage())

	this.startActivity(intent)
}

fun Context.launchCometChat(uid: Int, name: String, image: String?) {
	val user = User()
	user.uid = "user_$uid"
	if(image != null) user.avatar = image
	user.name = name
	startChatPage2(user)
}

fun Context.launchCometChat(configurations: User.() -> Unit) {
	val user = User()
	user.configurations()
	startChatPage2(user)
}

fun Context.openChatStore(v: View, uid: Int, name: String, image: String?) {
  Log.d(TAG, "startChatConversation $image")
  v.disable()
  val user = User()
  user.uid = "user_$uid" // Replace with the UID for the user to be created
  if(image != null) user.avatar = image
  user.name = name
  startChatPage(v, user)
}

fun Context.loginCometChat(externalUserId: Int) {
  CometChat.login(
    "user_$externalUserId",
    Constants.CHAT_AUTH_KEY,
    object : CometChat.CallbackListener<User>() {
      override fun onSuccess(user: User?) {
        user?.let {
          Log.d(TAG, "onSuccess: ${it.uid}")
        }
      }

      override fun onError(p0: CometChatException?) {
        Log.d(TAG, "failed: ${p0?.message}")
      }
    })
}

fun Context.logoutCometChat() {
  CometChat.logout(object : CometChat.CallbackListener<String?>() {
    override fun onSuccess(p0: String?) {
      Log.d(TAG, "onSuccess: ")
    }

    override fun onError(p0: CometChatException?) {
      Log.d(TAG, "failed: ")
    }
  })
}


