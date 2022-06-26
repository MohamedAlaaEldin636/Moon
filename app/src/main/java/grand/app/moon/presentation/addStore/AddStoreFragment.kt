package grand.app.moon.presentation.addStore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import grand.app.moon.databinding.FragmentAddStoreBinding
import grand.app.moon.core.extenstions.checkSelfPermissionGranted
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.more.SettingsViewModel
import grand.app.moon.presentation.splash.SplashActivity
import java.io.ByteArrayOutputStream
import java.util.*


@AndroidEntryPoint
class AddStoreFragment : BaseFragment<FragmentAddStoreBinding>() {
  var filePath: ValueCallback<Array<Uri>>? = null;
  private var filePathCallback: ValueCallback<Array<Uri>>? = null
  private val viewModel: SettingsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_add_store


  private val handler: Handler = object : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
      super.handleMessage(msg)
      when (msg.what) {
        1 -> binding.webview.goBack()
      }
    }
  }
  override
  fun setBindingVariables() {
    Log.d(TAG, "setBindingVariables: HERERERERERERERREERERERERER")
    binding.viewModel = viewModel
    val map = mutableMapOf<String, String>()
    map["language"] = LanguagesHelper.getCurrentLanguage()
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

    binding.webview.setOnKeyListener(object : View.OnKeyListener {
      override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK
          && event?.action == MotionEvent.ACTION_UP
          && binding.webview.canGoBack()) {
          handler.sendEmptyMessage(1);
          return true;
        }

        return false;
      }

    })


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
          permissionLocationRequest.launch(Manifest.permission.CAMERA)
//          pickImageViaChooser()
        }

        return true
      }

    }
    Log.d(TAG, "setBindingVariables: ${viewModel.getUrl()}")
    binding.webview.loadUrl(viewModel.getUrl(), map);

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
        Log.d(TAG, "shouldOverrideUrlLoading: ${link}")
        viewModel.saveUrl(link)
        if((!viewModel.browserHelper.isUser() && activity is HomeActivity) || (viewModel.browserHelper.isUser() && activity is AddStoreActivity)) {
          Intent(activity,SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
          }
        }
//        else if(viewModel.browserHelper.isUser() && activity is AddStoreActivity){
//          Intent(activity,AddStoreActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(this)
//          }
//        }
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
    Log.d(TAG, "onDestroy: ")
    super.onDestroy()
  }

}