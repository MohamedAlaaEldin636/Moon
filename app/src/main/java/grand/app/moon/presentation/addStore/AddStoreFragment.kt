package grand.app.moon.presentation.addStore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
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
import androidx.core.content.FileProvider
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

import java.io.File
import java.util.*

import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import grand.app.moon.helpers.utils.getBitmap
import grand.app.moon.helpers.utils.getUriFromBitmapRetrievedByCamera
import grand.app.moon.helpers.utils.handleCaptureImageRotation

import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


@AndroidEntryPoint
class AddStoreFragment : BaseFragment<FragmentAddStoreBinding>() {
  private var filePathCallback: ValueCallback<Array<Uri>>? = null
  private val viewModel: SettingsViewModel by viewModels()
  var imageUri: Uri? = null
  var fileCameraCapture: File? = null
  var galleryImageMutliple = -1

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
        Log.d(TAG, "onKey: HERE")
        if (keyCode == KeyEvent.KEYCODE_BACK
          && event?.action == MotionEvent.ACTION_UP
          && binding.webview.canGoBack()
        ) {
          Log.d(TAG, "onKey: WOIR")
          handler.sendEmptyMessage(1);
          return true;
        }

        return false;
      }

    })


    binding.webview.webChromeClient = object : WebChromeClient() {
      override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
      ): Boolean {

        Log.d(TAG, "onShowFileChooser: YES")
        fileChooserParams?.mode
        this@AddStoreFragment.filePathCallback = filePathCallback//?.onReceiveValue(null)
        fileChooserParams?.mode?.let {
          galleryImageMutliple = it
        }

        if (requireActivity().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
          && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          && requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA)
        ) {
          Log.d(TAG, "onShowFileChooser: WELL DONE")
          pickImageViaChooser()
        } else {
          Log.d(TAG, "onShowFileChooser: WELL WORKING")

          permissionLocationRequest.launch(
            arrayOf(
              Manifest.permission.READ_EXTERNAL_STORAGE,
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.CAMERA,
            )
          )


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

    binding.viewPopUP.showPopup(
      listOf(camera, gallery),
      listener = {
        Log.d(TAG, "pickImageViaChooser: on listener")
        when (it.title?.toString()) {
//        camera -> activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
          camera -> {
            imageUri = createImageUri()!!
            activityResultImageCameraFile.launch(imageUri)
          }
          else -> {
            val intent = Intent(Intent.ACTION_GET_CONTENT).also {
              it.type = "image/*"
              if(galleryImageMutliple == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE) it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            activityResultImageGallery.launch(
              intent
            )
          }
        }
      },
      onDismiss = {
        Log.d(TAG, "pickImageViaChooser: onDismiss")
        filePathCallback?.onReceiveValue(arrayOf())
      }
    )
  }

  private val activityResultImageCameraFile = registerForActivityResult(
    ActivityResultContracts.TakePicture()
  ) {
    Log.d(TAG, "pickImageViaChooser: on listener Fetch")
    if (it != null && it && imageUri != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val bitmap = handleCaptureImageRotation(fileCameraCapture,imageUri)
        bitmap?.let {
          imageUri = getUriFromBitmapRetrievedByCamera(it)
        }
      }
      filePathCallback?.onReceiveValue(arrayOf(imageUri!!))
    } else {
      filePathCallback?.onReceiveValue(arrayOf())
    }
  }


  val uris = ArrayList<Uri>()
  private val activityResultImageGallery = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {
    if (it.resultCode == Activity.RESULT_OK) {
      if(it.data?.clipData != null){
        val count = it.data?.clipData?.itemCount
        uris.clear()
        for(i in 0..count?.minus(1)!!){
          it.data?.clipData?.getItemAt(i)?.uri.let {
            it?.let { it1 -> uris.add(it1) }
          }
        }
        filePathCallback?.onReceiveValue(uris.toTypedArray())
      } else if(it.data?.data != null){
        val uri = it.data?.data ?: return@registerForActivityResult
        kotlin.runCatching {
          filePathCallback?.onReceiveValue(arrayOf(uri))
        }.getOrElse {
          Log.e(TAG, ": ${it.message}")
        }
      }
      else {
        filePathCallback?.onReceiveValue(arrayOf())
      }
    } else {
      filePathCallback?.onReceiveValue(arrayOf())
    }
  }

  fun createImageUri(): Uri? {
    fileCameraCapture = File(
      activity?.applicationContext?.filesDir,
      "camera_photo${SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH)}.png"
    )
    activity?.applicationContext?.let {
      return FileProvider.getUriForFile(it, "grand.app.moon.fileprovider", fileCameraCapture!!)
    }
    return null
  }


  private val permissionLocationRequest = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    when {
      permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
        && permissions[Manifest.permission.CAMERA] == true -> {
        pickImageViaChooser()
      }
      permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
        activityResultImageGallery.launch(
          Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
          )
        )
      }
      permissions[Manifest.permission.CAMERA] == true -> {
        activityResultImageCameraFile.launch(imageUri)
      }
      else -> {
        showMessage(getString(R.string.you_didn_t_accept_permission))
      }
    }
  }

  private val TAG = "AddStoreFragment"

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
        if ((!viewModel.browserHelper.isUser() && activity is HomeActivity)) {
          val homeActivity = activity as HomeActivity
          homeActivity.goHomePage()
          homeActivity.initStoreBtn()
          openActivity(AddStoreActivity::class.java)
        } else if ((viewModel.browserHelper.isUser() && activity is AddStoreActivity)) {
          (activity as AddStoreActivity).finishAffinity()
          Intent(activity, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
          }
        }
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