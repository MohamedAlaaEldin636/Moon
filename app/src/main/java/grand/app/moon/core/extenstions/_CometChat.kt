package grand.app.moon.core.extenstions

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import grand.app.moon.core.MyApplication
import grand.app.moon.presentation.base.extensions.disable
import grand.app.moon.presentation.base.extensions.enable
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
  v.context.startActivity(intent)
}

fun Context.openChatStore(v: View, uid: Int, name: String, image:String) {
  Log.d(TAG, "startChatConversation")
  v.disable()
  val user = User()
  user.uid = "store_$uid" // Replace with the UID for the user to be created
  user.avatar = image
  user.name = name
  startChatPage(v,user)
}

fun Context.loginCometChat(externalUserId: Int){
  CometChat.login("user_$externalUserId", Constants.CHAT_AUTH_KEY, object : CometChat.CallbackListener<User>() {
    override fun onSuccess(user: User?) {
      Log.d(TAG, "onSuccess: ")
    }
    override fun onError(p0: CometChatException?) {
      Log.d(TAG, "failed: ")
    }
  })
}

fun Context.logoutCometChat(){
  CometChat.logout(object : CometChat.CallbackListener<String?>() {
    override fun onSuccess(p0: String?) {
      Log.d(TAG, "onSuccess: ")
    }

    override fun onError(p0: CometChatException?) {
      Log.d(TAG, "failed: ")
    }
  })
}
