package grand.app.moon.presentation.base

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.facebook.FacebookSdk.getCacheDir
import es.dmoral.toasty.Toasty
import grand.app.moon.BuildConfig
import grand.app.moon.R
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.FilterFragmentDirections
import kotlinx.coroutines.Job
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.URLEncoder
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import grand.app.moon.domain.home.models.Store


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
      } else share(context, title, message)
    } catch (e: IOException) {
      Log.d(TAG, "share: ${e.message}")
      e.printStackTrace()
    }
  }

  open fun share(context: Context, title: String, message: String) {
//    val imagePath = File(getCacheDir(), "images")
//    val newFile = File(imagePath, "image.png")
//    val contentUri = FileProvider.getUriForFile(
//      context,
//      BuildConfig.APPLICATION_ID + ".fileprovider",
//      newFile
//    )
//    //
//    if (contentUri != null) {
//      val shareIntent = Intent()
//      shareIntent.action = Intent.ACTION_SEND
//      shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
//      shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
//      shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
//      shareIntent.type = "*/*"
//      shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name))
//      shareIntent.putExtra(
//        Intent.EXTRA_TEXT, """
//   $title
//   $message
//   """.trimIndent()
//      )
//      context.startActivity(
//        Intent.createChooser(
//          shareIntent,
//          context.resources.getString(R.string.share)
//        )
//      )
//    }

    val intent = Intent(Intent.ACTION_SEND)
    /*This will be the actual content you wish you share.*/
    /*This will be the actual content you wish you share.*/
//    val shareBody = message
    /*The type of the content is text, obviously.*/
    /*The type of the content is text, obviously.*/intent.type = "text/plain"
    /*Applying information Subject and Body.*/
    /*Applying information Subject and Body.*/intent.putExtra(
      Intent.EXTRA_SUBJECT,
      context.getString(R.string.app_name)
    )
    intent.putExtra(Intent.EXTRA_TEXT, title+"\n"+message)
    /*Fire!*/
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
  }



  fun toFilter(
    v: View, category_id: Int? = -1, category_name: String? = null, sub_category_id: Int? = -1,
    sub_category_name: String? = null,allow_change_category : Boolean = true, store_id: Int = -1,
    store:Store? = Store()
  ) {
    val bundle = Bundle()

    category_id?.let { bundle.putInt("category_id", it) }
    if (sub_category_id != null) {
      bundle.putInt("sub_category_id", sub_category_id)
    }
    bundle.putInt("store_id", store_id)
    bundle.putBoolean("allow_change_category",allow_change_category)

    category_name?.let {
      bundle.putString("category_name", it)
    }

    sub_category_name?.let {
      bundle.putString("sub_category_name", it)
    }

    store?.let {
      bundle.putSerializable("store", it)
    }


    v.findNavController()
      .navigate(R.id.to_filter, bundle, Constants.NAVIGATION_OPTIONS)

  }

  fun toFilterResult(v: View, request: FilterResultRequest) {

    v.findNavController()
      .navigate(FilterFragmentDirections.actionFilterFragmentToFilterResultsFragment(request))

  }


  private val TAG = "BaseViewModel"

  fun checkDeepLink(intent: Intent,v: View) {
    Log.d(TAG, "checkDeepLink: ")
    val action = intent.action
    val data = intent.data
    try {
      if (action != null && data != null) {
        Log.d(TAG, "checkDeepLink: ACTIOM")
        val link = data.toString()
        val parameters = link.split("/").toTypedArray()
        Log.d(TAG, "initDynamicLinkSetDefaultCountry: ${parameters[4]}")
        val id = parameters[5]
        when(parameters[4]){
          "advertisement" -> {
            Log.d(TAG, "initDynamicLinkSetDefaultCountry: done")
            v.findNavController().navigate(
              R.id.nav_ads, bundleOf(
                "id" to id.toInt(),
                "type" to 2
              )
            )
          }
          "shop" -> {
            v.findNavController().navigate(
              R.id.nav_store,
              bundleOf(
                "id" to id.toInt(),
                "type" to 3
              ),Constants.NAVIGATION_OPTIONS)
          }
        }
      }
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

}