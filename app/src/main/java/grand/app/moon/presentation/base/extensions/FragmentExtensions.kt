package grand.app.moon.presentation.base.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import grand.app.moon.R
import grand.app.moon.domain.utils.FailureStatus
import grand.app.moon.domain.utils.Resource.Failure
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.utils.*
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.facebook.FacebookSdk.getCacheDir
import grand.app.moon.core.extenstions.logout
import com.onesignal.OneSignal
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.convertToString
import grand.app.moon.core.extenstions.convertToStringArray
import grand.app.moon.core.extenstions.loginCometChat
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.presentation.home.HomeActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.ArrayList


fun Fragment.handleApiError(
  failure: Failure,
  retryAction: (() -> Unit)? = null,
  noDataAction: (() -> Unit)? = null,
  notActive: (() -> Unit)? = null,
  notActiveAction: (() -> Unit)? = null,
  noInternetAction: (() -> Unit)? = null
) {
  when (failure.failureStatus) {
    FailureStatus.EMPTY -> {
      noDataAction?.invoke()
      failure.message?.let { showNoApiErrorAlert(requireActivity(), it) }
    }
    FailureStatus.NO_INTERNET -> {
      noInternetAction?.invoke()
      showNoInternetAlert(requireActivity())
    }
    FailureStatus.TOKEN_EXPIRED -> {
      logout()
      openActivityAndClearStack(AuthActivity::class.java)
    }
    else -> showNoApiErrorAlert(requireActivity(), getString(R.string.some_error))
  }
}

fun Fragment.logout() {
  MyApplication.instance.logout()
}

/*
01010998759
1016171926
 */
private const val TAG = "FragmentExtensions"
fun Fragment.makeIntegrationWithRedirectHome(externalUserId: Int) {
  requireActivity().finishAffinity()
  OneSignal.setExternalUserId("user_$externalUserId")
  requireContext().loginCometChat(externalUserId)
  openActivityAndClearStack(HomeActivity::class.java)
}


fun Fragment.shareCustom(activity: Context, title: String, message: String, imageView: ImageView) {
  // save bitmap to cache directory
  try {
    try {
      imageView.invalidate()
    } catch (exception: Exception) {
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
        e.printStackTrace()
      }
    }
    if (bitmapDrawable != null && bitmapDrawable.bitmap != null && stream != null) {
      bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
      stream.close()
      share(activity, title, message)
    } else shareCustom(activity, title, message)
  } catch (e: IOException) {
    e.printStackTrace()
  }
}

fun Fragment.shareCustom(context: Context, title: String, message: String) {
  val intent = Intent(Intent.ACTION_SEND)
  intent.type = "text/plain"
  intent.putExtra(Intent.EXTRA_SUBJECT, title)
  intent.putExtra(Intent.EXTRA_TEXT, message)
  context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
}

fun Fragment.share(context: Context, title: String, message: String) {

  val shareIntent = Intent()
  shareIntent.action = Intent.ACTION_SEND
  shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
  shareIntent.type = "*/*"
  shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
  shareIntent.putExtra(
    Intent.EXTRA_TEXT, """
   $title
   $message
   """.trimIndent()
  )
  context.startActivity(
    Intent.createChooser(
      shareIntent,
      context.getString(R.string.share)
    )
  )
}

fun Fragment.hideKeyboard() = hideSoftInput(requireActivity())

fun Fragment.showNoInternetAlert() = showNoInternetAlert(requireActivity())

fun Fragment.showMessage(message: String?) = showMessage(requireContext(), message)

fun Fragment.showError(
  message: String,
  retryActionName: String? = null,
  action: (() -> Unit)? = null
) =
  requireView().showSnackBar(message, retryActionName, action)

fun Fragment.getMyColor(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)

fun Fragment.getMyDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(requireContext(), id)!!
fun Fragment.getMyDrawableVector(@DrawableRes id: Int) =
  ContextCompat.getDrawable(requireContext(), id)!!

fun Fragment.getMyString(id: Int) = resources.getString(id)

fun <A : Activity> Fragment.openActivityAndClearStack(activity: Class<A>) {
  requireActivity().openActivityAndClearStack(activity)
}

fun <A : Activity> Fragment.openActivity(activity: Class<A>) {
  requireActivity().openActivity(activity)
}

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
  findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.removeNavigationResultObserver(key: String = "result") =
  findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
  findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun Fragment.onBackPressedCustomAction(action: () -> Unit) {
//  requireActivity().onBackPressedDispatcher.addCallback(
//    viewLifecycleOwner,
//    object : OnBackPressedCallback(true) {
//      override
//      fun handleOnBackPressed() {
//        action()
//      }
//    }
//  )
}

fun Fragment.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
  findNavController().navigate(directions, navOptions)
}

fun Fragment.backToPreviousScreen() {
  findNavController().navigateUp()
}

@BindingAdapter(value = ["images", "scale"], requireAll = false)
fun setImages(sliderView: ImageSlider, images: ArrayList<String>?, scaleTypes: ScaleTypes?) {
  images?.let {
    val list = ArrayList<SlideModel>()
    for (image in images) {
      when (scaleTypes) {
        null -> list.add(SlideModel(image, ScaleTypes.CENTER_CROP))
        else -> {
          list.add(SlideModel(image, scaleTypes))
          Log.d(TAG, "setImages: ASDASDASDS")
        }
      }
    }
    sliderView.setImageList(list)
    sliderView.setItemClickListener(object : ItemClickListener {
      override fun onItemSelected(position: Int) {
        val gson = sliderView.context?.convertToStringArray(images)

        val uri = Uri.Builder()
          .scheme("zoomImagesPager")
          .authority("grand.app.images.pager")
          .appendPath(gson.orEmpty())
          .appendPath(position.toString())
          .build()
        val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
        sliderView.findNavController().navigate(request)

      }
    })
  }
}

@BindingAdapter("images")
fun setImagesAppTutrial(sliderView: ImageSlider, images: ArrayList<Advertisement>?) {
  images?.let {
    val list = ArrayList<SlideModel>()
    for (image in images) {
      list.add(SlideModel(image.image, ScaleTypes.FIT))
    }
    sliderView.setImageList(list)
    sliderView.setItemClickListener(object : ItemClickListener {
      override fun onItemSelected(position: Int) {
        sliderView.findNavController().navigate(
          R.id.nav_ads, bundleOf(
            "id" to images[position].id,
            "type" to 2,
          )
        )
      }
    })
  }
}

//drawableRotation

fun Fragment.openUrl(url: String) {
  val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
  try {
    startActivity(browserIntent)
  } catch (e: ActivityNotFoundException) {
    Toast.makeText(
      requireContext(),
      "Impossible to find an application for the market",
      Toast.LENGTH_LONG
    ).show()
  }
}

fun Fragment.startActivity(url: String) {
  val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
  try {
    startActivity(browserIntent)
  } catch (e: ActivityNotFoundException) {
    Toast.makeText(
      requireContext(),
      "Impossible to find an application for the market",
      Toast.LENGTH_LONG
    ).show()
  }
}
