package grand.app.moon.presentation.more

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentSettingsBinding
import grand.app.moon.databinding.FragmentWebBinding
import grand.app.moon.databinding.ReviewDialogBinding
import grand.app.moon.presentation.base.utils.Constants
import java.util.ArrayList

@AndroidEntryPoint
class WebFragment : BottomSheetDialogFragment() {

  val args: WebFragmentArgs by navArgs()
  lateinit var binding: FragmentWebBinding

  private val viewModel: SettingsViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web, container, false)
    binding.viewModel = viewModel
    binding.viewModel = viewModel
    binding.webview.webViewClient = WebViewClient()
    if (!args.url.contains("snapchat"))
      binding.webview.settings.javaScriptEnabled = true
    binding.webview.webViewClient = AppWebViewClients(binding.progress)
    binding.webview.loadUrl(args.url)
    return binding.root
  }



  //  @SuppressLint("SetJavaScriptEnabled")
//  @SuppressLint("SetJavaScriptEnabled")
//  override
//  fun setBindingVariables() {
//
//    binding.viewModel = viewModel
//    binding.webview.webViewClient = WebViewClient()
//    if (!args.url.contains("snapchat"))
//      binding.webview.settings.javaScriptEnabled = true
//    binding.webview.webViewClient = AppWebViewClients(binding.progress)
//    binding.webview.loadUrl(args.url)
//  }

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

    override fun onPageFinished(view: WebView, url: String) {
      // TODO Auto-generated method stub
      super.onPageFinished(view, url)
      progressBar.hide()
    }

    init {
      progressBar.show()
    }
  }

  override fun getTheme(): Int {
    return R.style.CustomBottomSheetDialogTheme;
  }
}