package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentComplainsAndSuggestionsBinding
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.PickImagesOrVideoHandler
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.ComplainsAndSuggestionsViewModel

@AndroidEntryPoint
class ComplainsAndSuggestionsFragment : BaseFragment<FragmentComplainsAndSuggestionsBinding>() {

	private val viewModel by viewModels<ComplainsAndSuggestionsViewModel>()

	var gettingImageHandler: PickImagesOrVideoHandler? = null
		private set

	override fun onCreate(savedInstanceState: Bundle?) {
		gettingImageHandler = PickImagesOrVideoHandler(
			this,
			PickImagesOrVideoHandler.SupportedMediaType.IMAGE,
			requestMultipleImages = false,
			getAnchor = { _binding?.imageTextView }
		) { uris, _, isImageNotVideo ->
			if (isImageNotVideo) {
				viewModel.image.value = uris.firstOrNull()
			}
		}

		super.onCreate(savedInstanceState)
	}

	override fun getLayoutId(): Int = R.layout.fragment_complains_and_suggestions

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return super.onCreateView(inflater, container, savedInstanceState)?.also {
			binding.countryCodePicker.registerCarrierNumberEditText(binding.phoneEditText)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.phone.observe(viewLifecycleOwner) {
			viewModel.showValidPhoneNum.value = binding.countryCodePicker.isValidFullNumber
		}
	}

}
