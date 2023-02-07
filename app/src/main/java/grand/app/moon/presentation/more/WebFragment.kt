package grand.app.moon.presentation.more

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import grand.app.moon.R
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.core.extenstions.hide
import grand.app.moon.core.extenstions.show
import grand.app.moon.databinding.FragmentWebBinding

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
    if (!args.url.contains("snapchat"))
      binding.webview.settings.javaScriptEnabled = true
    binding.webview.webViewClient = object : WebViewClient() {
	    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
		    findNavController().navigateUp()

		    return true
	    }

	    override fun onPageFinished(view: WebView?, url: String?) {
		    binding.progress.hide()
	    }
    }
	  binding.progress.show()
    Log.d(TAG, "onCreateView: ${args.url}")
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

  /*class AppWebViewClients(val progressBar: ProgressBar) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
      view.loadUrl(url)
      return true
    }

    override fun onPageFinished(view: WebView, url: String) {
      progressBar.hide()
    }

    init {
      progressBar.show()
    }
  }*/

  override fun onResume() {
    super.onResume()
    if(viewModel.isFirst)
      backToPreviousScreen()
    else
      viewModel.isFirst = true
  }

  override fun getTheme(): Int {
    return R.style.CustomBottomSheetDialogTheme
  }
}