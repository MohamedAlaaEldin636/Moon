package grand.app.moon.presentation.home.utils

import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.UsersRequest
import com.cometchat.pro.core.UsersRequest.UsersRequestBuilder
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import grand.app.moon.extensions.resumeSafely
import kotlin.coroutines.suspendCoroutine

object MACometChatUtils {

	suspend fun isUserOnline(id: Int): Boolean = suspendCoroutine { continuation ->
		val uid = "user_$id"

		CometChat.getUser(
			uid,
			object : CometChat.CallbackListener<User>() {
				override fun onSuccess(user: User?) {
					continuation.resumeSafely(user?.status == CometChatConstants.USER_STATUS_ONLINE)
				}

				override fun onError(p0: CometChatException?) {
					continuation.resumeSafely(false)
				}
			}
		)
	}

}
