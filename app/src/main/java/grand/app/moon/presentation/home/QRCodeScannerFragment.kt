package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.QrCodeScannerFragmentBinding
import grand.app.moon.extensions.navUpThenSetResultInBackStackEntrySavedStateHandleViaGson
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.viewModels.QRCodeScannerViewModel

@AndroidEntryPoint
class QRCodeScannerFragment : BaseFragment<QrCodeScannerFragmentBinding>() {

	private val viewModel by viewModels<QRCodeScannerViewModel>()

	private var codeScanner: CodeScanner? = null

	override fun getLayoutId(): Int = R.layout.qr_code_scanner_fragment

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		codeScanner = CodeScanner(requireActivity(), binding.scannerView)
		codeScanner?.decodeCallback = DecodeCallback {
			activity?.runOnUiThread {
				findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
					it.text.orEmpty(),
					QRCodeScannerFragment::class.java.name
				)
			}
		}
		binding.scannerView.setOnClickListener {
			codeScanner?.startPreview()
		}
	}

	override fun onResume() {
		super.onResume()
		codeScanner?.startPreview()
	}

	override fun onPause() {
		codeScanner?.releaseResources()
		super.onPause()
	}

}
