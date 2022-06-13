package grand.app.moon.presentation.more

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
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
import java.util.ArrayList

@AndroidEntryPoint
class AddStoreFragment : BaseFragment<FragmentAddStoreBinding>() {


  private val viewModel: SettingsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_add_store

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    binding.webview.webViewClient = WebViewClient()
    binding.webview.settings.javaScriptEnabled = true
    binding.webview.webViewClient = AppWebViewClients(binding.progress)
    val map = mutableMapOf<String,String>()
    map.put("language",LanguagesHelper.getCurrentLanguage())
    binding.webview.loadUrl("https://souqmoon.com/store/register",map)

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
}