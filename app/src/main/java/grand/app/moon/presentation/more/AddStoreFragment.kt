package grand.app.moon.presentation.more

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.webkit.*
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import grand.app.moon.databinding.FragmentAddStoreBinding
import grand.app.moon.databinding.FragmentSettingsBinding
import grand.app.moon.databinding.FragmentWebBinding
import grand.app.moon.presentation.base.utils.Constants
import im.delight.android.webview.AdvancedWebView
import androidx.core.app.ActivityCompat.startActivityForResult
import com.cometchat.pro.uikit.ui_resources.utils.MediaUtils.Companion.openCamera
import com.maproductions.mohamedalaa.shared.core.extensions.checkSelfPermissionGranted
import grand.app.moon.presentation.auth.profile.ProfileFragmentDirections
import grand.app.moon.presentation.home.HomeActivity
import java.io.ByteArrayOutputStream
import java.util.*


@AndroidEntryPoint
class AddStoreFragment : BaseFragment<FragmentAddStoreBinding>() {
  var filePath: ValueCallback<Array<Uri>>? = null;
  private var filePathCallback: ValueCallback<Array<Uri>>? = null


  private val viewModel: SettingsViewModel by viewModels()

  private var mUploadMessage: ValueCallback<Uri>? = null
  private val FILECHOOSER_RESULTCODE = 1

  override
  fun getLayoutId() = R.layout.fragment_add_store

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    Log.d(TAG, "setBindingVariables: YARRRRRRRRRRRRRRRRB")
//    binding.webview.webViewClient = WebViewClient()
//    binding.webview.settings.javaScriptEnabled = true
//    binding.webview.settings.javaScriptCanOpenWindowsAutomatically=true

//    binding.webview.webViewClient = AppWebViewClients(binding.progress)
    val map = mutableMapOf<String, String>()
    map["language"] = LanguagesHelper.getCurrentLanguage()

//    binding.webview.setListener(requireActivity(), this);
//    binding.webview.setMixedContentAllowed(false);
//    binding.webview.setMixedContentAllowed(true);
//    binding.webview.setDesktopMode(true);
    binding.webview.settings.setSupportZoom(false)
    binding.webview.settings.allowFileAccess = true;
    binding.webview.settings.allowContentAccess = true;
    binding.webview.settings.useWideViewPort = true;
    binding.webview.settings.domStorageEnabled = true;
    binding.webview.settings.javaScriptEnabled = true;
    binding.webview.settings.javaScriptCanOpenWindowsAutomatically = true;
    binding.webview.settings.pluginState = WebSettings.PluginState.ON;
    binding.webview.settings.setSupportMultipleWindows(true);
    binding.webview.webViewClient = MyWebViewClient()
//    binding.webview.webChromeClient = MyWebChromeClient()
    binding.webview.webChromeClient = object : WebChromeClient() {
      override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
      ): Boolean {

        this@AddStoreFragment.filePathCallback = filePathCallback//?.onReceiveValue(null)

        if (requireActivity().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
          && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          && requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA)
        ) {
          pickImageViaChooser()
//          activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        } else {
//          permissionLocationRequest.launch(Manifest.permission.CAMERA)
          pickImageViaChooser()
        }

        return true
      }

    }


//    binding.webview.webChromeClient = object: WebChromeClient() {
//
//
//      override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
//        super.onReceivedIcon(view, icon)
//        Log.d(TAG, "onReceivedIcon: ")
//      }
//
//      override fun onPermissionRequest(request: PermissionRequest?) {
//        super.onPermissionRequest(request)
//        Log.d(TAG, "onPermissionRequest: ")
//      }
//
//      override fun onShowFileChooser(
//        webView: WebView?,
//        filePathCallback: ValueCallback<Array<Uri>>?,
//        fileChooserParams: FileChooserParams?
//      ): Boolean {
//        Log.d(TAG, "onShowFileChooser: ")
//        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
//      }
//
//      override fun onCloseWindow(window: WebView?) {
//        super.onCloseWindow(window)
//        Log.d(TAG, "onCloseWindow: ")
//      }
//
//      override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
//        return super.onConsoleMessage(consoleMessage)
//        Log.d(TAG, "onConsoleMessage: ")
//      }
//
//
//
//      override fun onCreateWindow(
//        view: WebView?,
//        isDialog: Boolean,
//        isUserGesture: Boolean,
//        resultMsg: Message?
//      ): Boolean {
//        Log.d(TAG, "onCreateWindow: JREGERERERER")
//        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
//      }
////      override fun onCreateWindow(
////        view: WebView?,
////        isDialog: Boolean,
////        isUserGesture: Boolean,
////        resultMsg: Message?
////      ): Boolean {
////        Log.d(TAG, "onCreateWindow: CREATE WINDOW")
////
////        val newWebView = AdvancedWebView(context)
////        // myParentLayout.addView(newWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
////        // myParentLayout.addView(newWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
////        val transport = resultMsg!!.obj as WebView.WebViewTransport
////        transport.webView = newWebView
////        resultMsg.sendToTarget()
////
////        return true
////      }
////
////      override fun onShowFileChooser(
////        webView: WebView?,
////        filePathCallback: ValueCallback<Array<Uri>>?,
////        fileChooserParams: FileChooserParams?
////      ): Boolean {
////        Log.d(TAG, "onShowFileChooser: HERE")
//////        (requireActivity() as HomeActivity).filePath = filePathCallback
//////
//////        val contentIntent = Intent(Intent.ACTION_GET_CONTENT)
//////        contentIntent.type = "image/*"
//////        contentIntent.addCategory(Intent.CATEGORY_OPENABLE)
//////
//////        requireActivity().startActivityForResult(contentIntent, 1)
////
//////        openFileChooser(fileChooserParams)
////        return true
////      }
//
//
//      fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
//        mUploadMessage = uploadMsg
//        val i = Intent(Intent.ACTION_GET_CONTENT)
//        i.addCategory(Intent.CATEGORY_OPENABLE)
//        i.type = "image/*"
//        startActivityForResult(
//          Intent.createChooser(i, "File Chooser"),
//          FILECHOOSER_RESULTCODE
//        )
//      }
//
//      // For Android 3.0+
//      fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?) {
//        mUploadMessage = uploadMsg
//        val i = Intent(Intent.ACTION_GET_CONTENT)
//        i.addCategory(Intent.CATEGORY_OPENABLE)
//        i.type = "*/*"
//        activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
//
//      }
//
//      //For Android 4.1
//      fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?, capture: String?) {
//        mUploadMessage = uploadMsg
//        val i = Intent(Intent.ACTION_GET_CONTENT)
//        i.addCategory(Intent.CATEGORY_OPENABLE)
//        i.type = "image/*"
//        activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
////        requireActivity().startActivityForResult(
////          Intent.createChooser(i, "File Chooser"),
////          FILECHOOSER_RESULTCODE
////        )
//      }
//
//    }
    binding.webview.loadUrl("https://souqmoon.com/store/register", map);

  }

  private fun pickImageViaChooser() {
    val camera = getString(R.string.camera)
    val gallery = getString(R.string.gallery)

    binding.webview.showPopup(listOf(camera, gallery)) {
      when (it.title?.toString()) {
        camera -> activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        else -> activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
      }
    }
  }

  private val activityResultImageCamera = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {

    if (it.resultCode == Activity.RESULT_OK) {
      val bitmap = it.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult

      val uri = getUriFromBitmapRetrievedByCamera(bitmap)

      filePathCallback?.onReceiveValue(arrayOf(uri))
    }
  }

  private val activityResultImageGallery = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {
    if (it.resultCode == Activity.RESULT_OK) {
      val uri = it.data?.data ?: return@registerForActivityResult
      filePathCallback?.onReceiveValue(arrayOf(uri))
    }
  }


  private fun getUriFromBitmapRetrievedByCamera(bitmap: Bitmap): Uri {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
    val byteArray = stream.toByteArray()
    val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    val path = MediaStore.Images.Media.insertImage(
      requireContext().contentResolver,
      compressedBitmap,
      Date(System.currentTimeMillis()).toString() + "photo",
      null
    )
    return Uri.parse(path)
  }


  private val permissionLocationRequest = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted ->

    if (isGranted == true) {
      activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }
  }


  class MyWebChromeClient : WebChromeClient() {


  }


  private val TAG = "WebFragment"

  override fun onStop() {
    super.onStop()
  }

  private inner class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(
      view: WebView?,
      request: WebResourceRequest?
    ): Boolean {
      request?.url?.toString()?.also { link ->
//        if (link.isNotEmpty()) links += link
      }

      return false
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      super.onPageStarted(view, url, favicon)
      binding.progress.show()
    }

    override fun onReceivedError(
      view: WebView?,
      request: WebResourceRequest?,
      error: WebResourceError?
    ) {
      super.onReceivedError(view, request, error)
      binding.progress.hide()
    }


    override fun onPageFinished(view: WebView?, url: String?) {
      super.onPageFinished(view, url)
      binding.progress.hide()
    }
  }

  @SuppressLint("NewApi")
  override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume: ")
    binding.webview.onResume()
  }

  @SuppressLint("NewApi")
  override fun onPause() {
    binding.webview.onPause()
    // ...
    Log.d(TAG, "onPause: ")
    super.onPause()
  }

  override fun onDestroy() {
//    binding.webview.onDestroy()
    // ...
    Log.d(TAG, "onDestroy: ")
    super.onDestroy()
  }

}