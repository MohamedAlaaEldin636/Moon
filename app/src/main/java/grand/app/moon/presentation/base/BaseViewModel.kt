package grand.app.moon.presentation.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import com.facebook.FacebookSdk.getCacheDir
import es.dmoral.toasty.Toasty
import grand.app.moon.BuildConfig
import grand.app.moon.R
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.CometChatResource
import grand.app.moon.domain.utils.ICometChat
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.extensions.disable
import grand.app.moon.presentation.base.extensions.enable
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.URLEncoder

open class BaseViewModel : ViewModel(), Observable {
  private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()
  val show = ObservableBoolean(false)
  val isGrid2 = ObservableBoolean(true)

  protected var job: Job = Job()

  var clickEvent: MutableLiveData<Int> = MutableLiveData()
  var submitEvent: MutableLiveData<String> = MutableLiveData()
  fun clickEvent(action: Int) {
    clickEvent.value = action
  }

  fun submitEvent(action: String) {
    submitEvent.value = action
  }

  fun showError(context: Context, message: String) {
    Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
  }

  fun showInfo(context: Context, message: String) {
    Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
  }

  fun callPhone(context: Context, phone: String) {
    val call = Uri.parse("tel:$phone")
    val surf = Intent(Intent.ACTION_DIAL, call)
    context.startActivity(surf)
  }

  override fun addOnPropertyChangedCallback(
    callback: Observable.OnPropertyChangedCallback
  ) {
    callbacks.add(callback)
  }

  override fun removeOnPropertyChangedCallback(
    callback: Observable.OnPropertyChangedCallback
  ) {
    callbacks.remove(callback)
  }

  /**
   * Notifies observers that all properties of this instance have changed.
   */
  fun notifyChange() {
    callbacks.notifyCallbacks(this, 0, null)
  }

  /**
   * Notifies observers that a specific property has changed. The getter for the
   * property that changes should be marked with the @Bindable annotation to
   * generate a field in the BR class to be used as the fieldId parameter.
   *
   * @param fieldId The generated BR id for the Bindable field.
   */
  fun notifyPropertyChanged(fieldId: Int) {
    callbacks.notifyCallbacks(this, fieldId, null)
  }


  fun shareWhatsapp(v: View, title: String, desc: String, phone: String) {
    var url = "https://api.whatsapp.com/send?phone=${phone}"
    val i = Intent(Intent.ACTION_VIEW)
    url += "&text=" + URLEncoder.encode(
      title + "\n" + desc,
      "UTF-8"
    )
    try {
      i.setPackage("com.whatsapp")
      i.data = Uri.parse(url)
      v.context.startActivity(i)
    } catch (e: Exception) {
      try {
        i.setPackage("com.whatsapp.w4b")
        i.data = Uri.parse(url)
        v.context.startActivity(i)
      } catch (exception: Exception) {
        showInfo(v.context, v.context.getString(R.string.please_install_whatsapp_on_your_phone));
      }
    }
  }


  open fun share(
    context: Context,
    title: String,
    message: String,
    imageView: ImageView
  ) {
    try {
      try {
        imageView.invalidate()
      } catch (exception: Exception) {
        Log.d(TAG, "share: ${exception.message}")
        exception.printStackTrace()
      }
      var bitmapDrawable: BitmapDrawable? = null
      var stream: FileOutputStream? = null
      if (imageView.drawable != null) {
        try {
          bitmapDrawable = imageView.drawable as BitmapDrawable
          val cachePath = File(getCacheDir(), "images")
          if (!cachePath.exists()) cachePath.mkdirs() // don't forget to make the directory
          stream = FileOutputStream("$cachePath/image.png") // overwrites this image every time
        } catch (e: Exception) {
          Log.d(TAG, "share: ${e.message}")
          e.printStackTrace()
        }
      }
      if (bitmapDrawable != null && bitmapDrawable.bitmap != null && stream != null) {
        bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
        share(context, title, message)
      } else  share(context, title, message)
    } catch (e: IOException) {
      Log.d(TAG, "share: ${e.message}")
      e.printStackTrace()
    }
  }

  open fun share(context: Context, title: String, message: String) {
    val imagePath = File(getCacheDir(), "images")
    val newFile = File(imagePath, "image.png")
    val contentUri = FileProvider.getUriForFile(
      context,
      BuildConfig.APPLICATION_ID + ".fileprovider",
      newFile
    )
    //
    if (contentUri != null) {
      val shareIntent = Intent()
      shareIntent.action = Intent.ACTION_SEND
      shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
      shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
      shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
      shareIntent.type = "*/*"
      shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name))
      shareIntent.putExtra(
        Intent.EXTRA_TEXT, """
   $title
   $message
   """.trimIndent()
      )
      context.startActivity(
        Intent.createChooser(
          shareIntent,
          context.resources.getString(R.string.share)
        )
      )
    }
  }


  private val TAG = "BaseViewModel"

  fun startChatConversation(v: View, uid: String) {
    Log.d(TAG, "startChatConversation")
    v.disable()
    val user = User()
    user.uid = uid // Replace with the UID for the user to be created

    CometChat.login(user.uid, Constants.CHAT_AUTH_KEY, object : CometChat.CallbackListener<User>() {
      override fun onSuccess(user: User?) {
        Log.d(TAG, "ologin")
//        if (user != null) {
//          startChatPage(v, user)
//        }
      }

      override fun onError(p0: CometChatException?) {
        Log.d(TAG, "onError: " + p0?.code.toString())
        if (p0?.code.toString() == "ERR_UID_NOT_FOUND") {
          Log.d(TAG, "onError: HERER code")
          CometChat.createUser(
            user,
            Constants.CHAT_AUTH_KEY,
            object : CometChat.CallbackListener<User>() {
              override fun onSuccess(p0: User?) {
                Log.d(TAG, "onSuccess: DONE")
                startChatConversation(v, uid)
              }

              override fun onError(p0: CometChatException?) {
                Log.d(TAG, "onErrorAGAIN: " + p0?.message)
                v.enable()
              }
            })
        }
      }

    })
  }

  fun logoutUser(): Boolean {
    val loginUser = CometChat.getLoggedInUser()
    if (loginUser != null) {
      CometChat.logout(object : CometChat.CallbackListener<String>() {
        override fun onSuccess(p0: String?) {
          return
        }

        override fun onError(p0: CometChatException?) {
        }
      })
    }
    return true
  }

  fun loginUser(
    viewmodel: BaseViewModel,
    uid: String,
    name: String?,
    image: String?,
    iCometChat: ICometChat
  ) {
    Log.d(TAG, "loginUser")
    logoutUser()
    val user = User()
    user.uid = uid // Replace with the UID for the user to be created
    name?.let {
      user.name = it
    }
    user.name = name ?: "guest"
    image?.let {
      user.avatar = it
    }
    user.link = image

    CometChat.login(user.uid, Constants.CHAT_AUTH_KEY, object : CometChat.CallbackListener<User>() {
      override fun onSuccess(user: User?) {
        Log.d(TAG, "ologin")
        if (user != null) {
          iCometChat.setLoggedIn(true)
        }
      }

      override fun onError(p0: CometChatException?) {
        Log.d(TAG, "onError: " + p0?.code.toString())
        if (p0?.code.toString() == "ERR_UID_NOT_FOUND") {
          Log.d(TAG, "onError: HERER")
          CometChat.createUser(
            user,
            Constants.CHAT_AUTH_KEY,
            object : CometChat.CallbackListener<User>() {
              override fun onSuccess(p0: User?) {
                Log.d(TAG, "onSuccess: DONE")

                CometChat.login(
                  user.uid,
                  Constants.CHAT_AUTH_KEY,
                  object : CometChat.CallbackListener<User>() {
                    override fun onSuccess(user: User?) {
                      Log.d(TAG, "ologin")
                      if (user != null) {
                        iCometChat.setLoggedIn(true)
                      }
                    }

                    override fun onError(p0: CometChatException?) {
                      Log.d(TAG, "onError ${p0?.code}")

                    }
                  })
              }

              override fun onError(p0: CometChatException?) {
                Log.d(TAG, "onError: CometChatException create User ${p0?.code}")
                iCometChat.setLoggedIn(false)
              }
            })
        }
      }

    })
  }

  private fun startChatPage(v: View, user: User) {
    v.enable()
    Log.d(TAG, "startChatPage")

    val intent = Intent(v.context, CometChatMessageListActivity::class.java)
    intent.putExtra(UIKitConstants.IntentStrings.UID, user.uid)
    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.avatar)
    intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.status)
    intent.putExtra(UIKitConstants.IntentStrings.NAME, user.name)
    intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER)
    intent.putExtra(UIKitConstants.IntentStrings.LINK, user.link)
    v.context.startActivity(intent)
  }


}