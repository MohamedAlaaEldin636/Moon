package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentContactUs2Binding
import grand.app.moon.extensions.PickImagesOrVideoHandler
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.ContactUs2ViewModel

@AndroidEntryPoint
class ContactUs2Fragment : BaseFragment<FragmentContactUs2Binding>() {

	private val viewModel by viewModels<ContactUs2ViewModel>()

	var gettingImageHandler: PickImagesOrVideoHandler? = null
		private set

	override fun onCreate(savedInstanceState: Bundle?) {
		gettingImageHandler = PickImagesOrVideoHandler(
			this,
			PickImagesOrVideoHandler.SupportedMediaType.BOTH,
			3 * 60,
			requestMultipleImages = false,
			getAnchor = { _binding?.imageTextView }
		) { uris, _, _ ->
			viewModel.image.value = uris.firstOrNull()
		}

		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoShop.getContactUsData()
			}
		) {
			viewModel.response = it

			viewModel.adapter.submitList(it)
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_contact_us_2

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

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)

		viewModel.phone.observe(viewLifecycleOwner) {
			viewModel.showValidPhoneNum.value = binding.countryCodePicker.isValidFullNumber
		}

		viewModel.selectedTypeOfData.observe(viewLifecycleOwner) {
			when (it) {
				null -> {
					binding.recyclerView.adapter = null
				}
				else -> {
					viewModel.adapter.notifyDataSetChanged()
				}
			}
		}
	}

	enum class Selection {
		EMAIL, PHONE, LOCATION
	}

}
