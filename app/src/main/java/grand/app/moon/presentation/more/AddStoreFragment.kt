package grand.app.moon.presentation.more

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Message
import android.util.Log
import android.webkit.*
import android.widget.ProgressBar
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
import java.util.ArrayList
import androidx.core.app.ActivityCompat.startActivityForResult
import grand.app.moon.presentation.home.HomeActivity


@AndroidEntryPoint
class AddStoreFragment : BaseFragment<FragmentAddStoreBinding>(), AdvancedWebView.Listener {
  var filePath: ValueCallback<Array<Uri>>? = null;


  private val viewModel: SettingsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_add_store

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
//    binding.webview.webViewClient = WebViewClient()
//    binding.webview.settings.javaScriptEnabled = true
//    binding.webview.settings.javaScriptCanOpenWindowsAutomatically=true

//    binding.webview.webViewClient = AppWebViewClients(binding.progress)
    val map = mutableMapOf<String,String>()
    map.put("language",LanguagesHelper.getCurrentLanguage())

    binding.webview.setListener(requireActivity(), this);
    binding.webview.setMixedContentAllowed(false);
    binding.webview.loadUrl("https://souqmoon.com/store/register",map);
    binding.webview.setMixedContentAllowed(true);
    binding.webview.setDesktopMode(true);
    binding.webview.settings.allowFileAccess = true;
    binding.webview.settings.allowContentAccess = true;
    binding.webview.settings.useWideViewPort = true;
    binding.webview.settings.pluginState = WebSettings.PluginState.OFF;


//    if (Build.VERSION.SDK_INT < 8) {
//      binding.webview.settings.plug = WebSettings.PluginState.OFF;
//    } else {
      binding.webview.settings.pluginState = WebSettings.PluginState.ON;
//    }

    binding.webview.settings.javaScriptEnabled =true;

    binding.webview.settings.setSupportMultipleWindows(true);
    binding.webview.webChromeClient = object: WebChromeClient() {

      override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
      ): Boolean {
        val newWebView = AdvancedWebView(context)
        // myParentLayout.addView(newWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // myParentLayout.addView(newWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        val transport = resultMsg!!.obj as WebView.WebViewTransport
        transport.webView = newWebView
        resultMsg.sendToTarget()

        return true
      }

      override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
      ): Boolean {
        (requireActivity() as HomeActivity).filePath = filePathCallback

        val contentIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentIntent.type = "*/*"
        contentIntent.addCategory(Intent.CATEGORY_OPENABLE)

        requireActivity().startActivityForResult(contentIntent, 1)

        return true
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    Log.d(TAG, "onActivityResult: YES")
    super.onActivityResult(requestCode, resultCode, data)
  }

  private val TAG = "WebFragment"

  override fun onStop() {
    super.onStop()
  }


  class AppWebViewClients(val progressBar: ProgressBar) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
      // TODO Auto-generated method stub
      view.loadUrl(url)
      return true
    }

    override fun shouldInterceptRequest(
      view: WebView?,
      request: WebResourceRequest?
    ): WebResourceResponse? {
      request?.let {
//        Log.d("hwere", "shouldInterceptRequest: HERRE")
//        it.requestHeaders.put("language",LanguagesHelper.getCurrentLanguage())
      }
      return super.shouldInterceptRequest(view, request)
    }

    override fun onPageFinished(view: WebView, url: String) {
      // TODO Auto-generated method stub
      super.onPageFinished(view, url)
      progressBar.hide()
    }

    init {
      progressBar.show()
    }
  }

  override fun onPageStarted(url: String?, favicon: Bitmap?) {
  }

  override fun onPageFinished(url: String?) {
  }

  override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
  }

  override fun onDownloadRequested(
    url: String?,
    suggestedFilename: String?,
    mimeType: String?,
    contentLength: Long,
    contentDisposition: String?,
    userAgent: String?
  ) {

  }

  override fun onExternalPageRequest(url: String?) {
  }

  @SuppressLint("NewApi")
  override fun onResume() {
    super.onResume()
    binding.webview.onResume()
  }

  @SuppressLint("NewApi")
  override fun onPause() {
    binding.webview.onPause()
    // ...
    super.onPause()
  }

  override fun onDestroy() {
    binding.webview.onDestroy()
    // ...
    super.onDestroy()
  }

}