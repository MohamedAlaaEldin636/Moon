package grand.app.moon.presentation.myStore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import grand.app.moon.core.extenstions.actOnGetIfNotInitialValueOrGetLiveData
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentCreateStoreBinding
import grand.app.moon.extensions.*
import grand.app.moon.extensions.compose.GlideImageViaXmlModel
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.presentation.myAds.LocationSelectionFragment
import grand.app.moon.presentation.myAds.addAdvFinalPage.CameraUtils
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myStore.viewModel.CreateStoreViewModel

@AndroidEntryPoint
class CreateStoreFragment : BaseFragment<FragmentCreateStoreBinding>(), PermissionsHandler.Listener {

	private val viewModel by viewModels<CreateStoreViewModel>()

	var permissionsHandlerForProfileImage: PermissionsHandler? = null
		private set

	private val activityResultImageCameraFile = registerForActivityResult(
		ActivityResultContracts.TakePicture()
	) { result ->
		if (result != null && result) {
			CameraUtils.imageUri?.also { imageUri ->
				val value = GlideImageViaXmlModel.IUri(imageUri)

				when (CameraUtils.tag) {
					AppConsts.COVER_IMAGE -> viewModel.backgroundImage.value = value
					AppConsts.LOGO_IMAGE -> viewModel.profileImage.value = value
				}
			}
		}
	}

	private val activityResultImageGallery = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		MyLogger.e("on result -> it.resultCode == Activity.RESULT_OK ${it.resultCode} ${Activity.RESULT_OK}")
		if (it.resultCode == Activity.RESULT_OK) {
			it?.data?.data?.also { imageUri ->
				val value = GlideImageViaXmlModel.IUri(imageUri)

				when (CameraUtils.tag) {
					AppConsts.COVER_IMAGE -> viewModel.backgroundImage.value = value
					AppConsts.LOGO_IMAGE -> viewModel.profileImage.value = value
				}
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		permissionsHandlerForProfileImage = PermissionsHandler(
			this,
			lifecycle,
			requireContext(),
			buildList {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					add(Manifest.permission.READ_MEDIA_IMAGES)
				}else {
					add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
					add(Manifest.permission.READ_EXTERNAL_STORAGE)
				}
				add(Manifest.permission.CAMERA)
			},
			this
		)

		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.useCaseShop.getShopInfo()
			}
		) { responseShopDetails ->
			viewModel.response.value = responseShopDetails

			val adsPhone = responseShopDetails.adsPhone.orEmpty()
			if (adsPhone.isNotEmpty()) {
				viewModel.activatedAdvertisingPhone.value = adsPhone
				val countryCode = responseShopDetails.adsCountryCode?.trimFirstPlusIfExistsOrEmpty()?.toIntOrNull()
				if (countryCode != null) {
					MyLogger.e("dhiuashdia 1 -> $adsPhone")
					viewModel.initialAdsPhoneCountryCode.value = countryCode
				}
			}
			val whatsappPhone = responseShopDetails.whatsappPhone.orEmpty()
			if (whatsappPhone.isNotEmpty()) {
				viewModel.activatedWhatsAppPhone.value = whatsappPhone
				val countryCode = responseShopDetails.whatsappCountryCode?.trimFirstPlusIfExistsOrEmpty()?.toIntOrNull()
				if (countryCode != null) {
					viewModel.initialWhatsAppPhoneCountryCode.value = countryCode
				}
			}

			handleRetryAbleActionOrGoBack(
				action = {
					viewModel.useCaseShop.getCitiesWithAreas()
				}
			) { cities ->
				viewModel.cities.value = cities

				val areas = cities.firstOrNull { it.id == responseShopDetails.cityId }?.areas.orEmpty()
				viewModel.areas.value = areas

				viewModel.selectedCity.value = cities.firstOrNull { it.id == responseShopDetails.cityId }
				viewModel.selectedArea.value = areas.firstOrNull { it.id == responseShopDetails.areaId }
			}
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return super.onCreateView(inflater, container, savedInstanceState)?.also {
			binding.countryCodePickerForAdsPhone.registerCarrierNumberEditText(binding.adsPhoneEditText)
			binding.countryCodePickerForWhatsAppPhone.registerCarrierNumberEditText(binding.whatsAppPhoneEditText)
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_create_store

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.advertisingPhone.observe(viewLifecycleOwner) {
			viewModel.showValidPhoneNumForAdsPhone.value = binding.countryCodePickerForAdsPhone.isValidFullNumber
		}
		viewModel.whatsAppPhone.observe(viewLifecycleOwner) {
			viewModel.showValidPhoneNumForWhatsAppPhone.value = binding.countryCodePickerForWhatsAppPhone.isValidFullNumber
		}

		viewModel.initialAdsPhoneCountryCode.observe(viewLifecycleOwner, object : Observer<Int?> {
			override fun onChanged(code: Int?) {
				if (code != null && viewModel.advertisingPhone.value.isNullOrEmpty()) {
					binding.countryCodePickerForAdsPhone.setCountryForPhoneCode(code)
					MyLogger.e("dhiuashdia 2 -> ${viewModel.response.value?.adsPhone} ==== ${viewModel.activatedAdvertisingPhone.value}")
					viewModel.advertisingPhone.value = viewModel.response.value?.adsPhone
					viewModel.initialAdsPhoneCountryCode.removeObserver(this)
				}
			}
		})
		viewModel.initialWhatsAppPhoneCountryCode.observe(viewLifecycleOwner, object : Observer<Int?> {
			override fun onChanged(code: Int?) {
				if (code != null && viewModel.whatsAppPhone.value.isNullOrEmpty()) {
					binding.countryCodePickerForWhatsAppPhone.setCountryForPhoneCode(code)
					viewModel.whatsAppPhone.value = viewModel.response.value?.whatsappPhone
					viewModel.initialWhatsAppPhoneCountryCode.removeObserver(this)
				}
			}
		})

		findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
			LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
			"",
			viewLifecycleOwner,
			{ !it.isNullOrEmpty() }
		) {
			viewModel.locationData.value = it.fromJsonInlinedOrNull<LocationData>()
		}
	}

	override fun onAllPermissionsAccepted() {
		val view = (when (CameraUtils.tag) {
			AppConsts.COVER_IMAGE -> _binding?.logoImageView
			AppConsts.LOGO_IMAGE -> _binding?.backgroundImageView
			else -> null
		}) ?: return

		val cameraString = getString(R.string.camera)
		val galleryString = getString(R.string.gallery)
		view.showPopup(
			listOf(cameraString, galleryString),
			listener = {
				if (cameraString == it.title.toStringOrEmpty()) {
					pickImageFromCamera()
				}else {
					pickImageFromGallery()
				}
			}
		)
	}

	override fun onSubsetPermissionsAccepted(permissions: Map<String, Boolean>) {
		if (permissions[Manifest.permission.CAMERA] == true) {
			pickImageFromCamera()
		}else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
			&& permissions[Manifest.permission.READ_MEDIA_IMAGES] == true) {
			pickImageFromGallery()
		}else if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
			&& permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
			pickImageFromGallery()
		}
	}

	private fun pickImageFromCamera() {
		activityResultImageCameraFile.launch(
			CameraUtils.createImageUri((this as? Fragment)?.activity ?: return)
		)
	}

	private fun pickImageFromGallery() {
		val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

		activityResultImageGallery.launchSafely(
			intent.createChooserMA(getString(R.string.pick_image))
		)
	}

}
