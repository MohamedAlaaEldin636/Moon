package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.myStore.ResponseMyStoreDetails
import grand.app.moon.extensions.*
import grand.app.moon.extensions.compose.GlideImageViaXmlModel
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.presentation.auth.confirmCode.ConfirmCodeFragment
import grand.app.moon.presentation.myAds.addAdvFinalPage.CameraUtils
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myStore.CreateStoreFragment
import grand.app.moon.presentation.myStore.model.ResponseArea
import grand.app.moon.presentation.myStore.model.ResponseCity
import javax.inject.Inject

@HiltViewModel
class CreateStoreViewModel @Inject constructor(
	application: Application,
	val userLocalUseCase: UserLocalUseCase,
	val useCaseShop: RepositoryPackages,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val showValidPhoneNumForAdsPhone = MutableLiveData(false)
	val showValidPhoneNumForWhatsAppPhone = MutableLiveData(false)

	val initialAdsPhoneCountryCode = MutableLiveData<Int?>(null)
	val initialWhatsAppPhoneCountryCode = MutableLiveData<Int?>(null)

	val response = MutableLiveData<ResponseMyStoreDetails>()

	val backgroundImage = response.mapNullableToMutableLiveData<ResponseMyStoreDetails, GlideImageViaXmlModel> {
		GlideImageViaXmlModel.IString(it?.coverImage.orEmpty())
	}

	val profileImage = response.mapNullableToMutableLiveData<ResponseMyStoreDetails, GlideImageViaXmlModel> {
		GlideImageViaXmlModel.IString(it?.logoImage.orEmpty())
	}

	val storeName = response.mapNullableToMutableLiveData {
		it?.storeName.orEmpty()
	}

	val userName = response.mapNullableToMutableLiveData {
		it?.userName.orEmpty()
	}

	val cities = MutableLiveData(emptyList<ResponseCity>())
	val selectedCity = MutableLiveData<ResponseCity?>()
	val cityName = selectedCity.map { it?.name.orEmpty() }

	val areas = MutableLiveData(emptyList<ResponseArea>())
	val selectedArea = MutableLiveData<ResponseArea?>()
	val areaName = selectedArea.map { it?.name.orEmpty() }

	val enableAreaField = areas.map { it.isNullOrEmpty().not() }

	val locationData = response.mapNullableToMutableLiveData { response ->
		response?.latitude?.let { latitude ->
			response.longitude?.let { longitude ->
				response.address?.let { address ->
					LocationData(latitude, longitude, address)
				}
			}
		}
	}
	val address = locationData.map { it?.address.orEmpty() }

	val description = response.mapNullableToMutableLiveData {
		it?.description.orEmpty()
	}

	val advertisingLink = response.mapNullableToMutableLiveData {
		it?.advertisingWebsite.orEmpty()
	}
	val email = response.mapNullableToMutableLiveData {
		it?.email.orEmpty()
	}
	val websiteLink = response.mapNullableToMutableLiveData {
		it?.websiteLink.orEmpty()
	}
	val advertisingPhone = response.mapNullableToMutableLiveData {
		it?.adsPhone.orEmpty()
	}
	val activatedAdvertisingPhone = MutableLiveData("")
	val whatsAppPhone = response.mapNullableToMutableLiveData {
		it?.whatsappPhone.orEmpty()
	}
	val activatedWhatsAppPhone = MutableLiveData("")
	val taxNumber = response.mapNullableToMutableLiveData {
		it?.taxNumber.orEmpty()
	}
	val showNotActivatedAdsPhone = switchMapMultiple2(advertisingPhone, activatedAdvertisingPhone) {
		val adsPhone = advertisingPhone.value.trimAllWhitespaces()
		val activatedAdsPhone = activatedAdvertisingPhone.value.trimAllWhitespaces()
		(adsPhone.isEmpty() || adsPhone != activatedAdsPhone)
	}
	val showNotActivatedWhatsAppPhone = switchMapMultiple2(whatsAppPhone, activatedWhatsAppPhone) {
		val whatsAppPhone = whatsAppPhone.value.trimAllWhitespaces()
		val activatedWhatsAppPhone = activatedWhatsAppPhone.value.trimAllWhitespaces()
		(whatsAppPhone.isEmpty() || whatsAppPhone != activatedWhatsAppPhone)
	}

	fun changeLogoImage(view: View) {
		val fragment = view.findFragmentOrNull<CreateStoreFragment>() ?: return

		CameraUtils.tag = AppConsts.LOGO_IMAGE

		fragment.permissionsHandlerForProfileImage?.actOnAllPermissionsAcceptedOrRequestPermissions()
	}

	fun changeCoverImage(view: View) {
		val fragment = view.findFragmentOrNull<CreateStoreFragment>() ?: return

		CameraUtils.tag = AppConsts.COVER_IMAGE

		fragment.permissionsHandlerForProfileImage?.actOnAllPermissionsAcceptedOrRequestPermissions()
	}

	fun selectCity(view: View) {
		view.showPopup(
			cities.value.orEmpty().map { it.name.orEmpty() },
			listener = { menuItem ->
				val newSelectedCity = cities.value.orEmpty().firstOrNull { it.name == menuItem.title.toStringOrEmpty() }

				if (newSelectedCity?.id != selectedCity.value?.id) {
					selectedCity.value = newSelectedCity

					areas.value = newSelectedCity?.areas.orEmpty()
					selectedArea.value = null
				}
			}
		)
	}

	fun selectArea(view: View) {
		if (areas.value.orEmpty().isEmpty()) return

		view.showPopup(
			areas.value.orEmpty().map { it.name.orEmpty() },
			listener = { menuItem ->
				selectedArea.value = areas.value.orEmpty().firstOrNull { it.name == menuItem.title.toStringOrEmpty() }
			}
		)
	}

	fun selectLocation(view: View) {
		view.findFragmentOrNull<CreateStoreFragment>()?.findNavController()?.navDeepToLocationSelection(
			locationData.value?.latitude,
			locationData.value?.longitude
		)
	}

	fun activateAdsPhone(view: View) {
		val fragment = view.findFragmentOrNull<CreateStoreFragment>() ?: return

		if (showNotActivatedAdsPhone.value != true) {
			return
		}else if (showValidPhoneNumForAdsPhone.value != true) {
			return fragment.showError(fragment.getString(R.string.contact_phone_num_is_invalid))
		}

		val adsCountryCode = fragment.binding.countryCodePickerForAdsPhone.selectedCountryCodeWithPlus.orEmpty()
		val adsPhone = fragment.binding.countryCodePickerForAdsPhone.fullNumberWithPlus.orEmpty()
			.removePrefix(adsCountryCode)

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.sendCode(adsCountryCode, adsPhone, true)
			}
		) {
			fragment.setFragmentResultListenerUsingJson<Boolean>(ConfirmCodeFragment.KEY_FRAGMENT_RESULT_VERIFICATION_SUCCEEDED) {
				if (it) {
					activatedAdvertisingPhone.value = advertisingPhone.value
				}
			}

			view.findNavController().navigateDeepLinkWithOptions(
				"confirmCode",
				"grand.app.moon.confirm.code",
				paths = arrayOf(adsCountryCode, adsPhone, ConfirmCodeFragment.TYPE_ADS_PHONE)
			)
		}
	}
	fun activateWhatsAppPhone(view: View) {
		val fragment = view.findFragmentOrNull<CreateStoreFragment>() ?: return

		if (showNotActivatedWhatsAppPhone.value != true) {
			return
		}else if (showValidPhoneNumForWhatsAppPhone.value != true) {
			return fragment.showError(fragment.getString(R.string.whatsapp_phone_num_is_invalid))
		}

		val whatsAppCountryCode = fragment.binding.countryCodePickerForWhatsAppPhone.selectedCountryCodeWithPlus.orEmpty()
		val whatsAppPhone = fragment.binding.countryCodePickerForWhatsAppPhone.fullNumberWithPlus.orEmpty()
			.removePrefix(whatsAppCountryCode)

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.sendCode(whatsAppCountryCode, whatsAppPhone, false)
			}
		) {
			fragment.setFragmentResultListenerUsingJson<Boolean>(ConfirmCodeFragment.KEY_FRAGMENT_RESULT_VERIFICATION_SUCCEEDED) {
				if (it) {
					activatedWhatsAppPhone.value = this.whatsAppPhone.value
				}
			}

			view.findNavController().navigateDeepLinkWithOptions(
				"confirmCode",
				"grand.app.moon.confirm.code",
				paths = arrayOf(whatsAppCountryCode, whatsAppPhone, ConfirmCodeFragment.TYPE_WHATSAPP_PHONE)
			)
		}
	}

	fun createOrUpdateStore(view: View) {
		val fragment = view.findFragmentOrNull<CreateStoreFragment>() ?: return
		val context = fragment.context ?: return

		if (
			backgroundImage.value == null || profileImage.value == null || storeName.value.isNullOrEmpty()
			|| userName.value.isNullOrEmpty() || selectedCity.value == null || selectedArea.value == null
			|| locationData.value == null || description.value.isNullOrEmpty()
		) {
			return fragment.showError(fragment.getString(R.string.fill_required_fields))
		}

		val regex = Regex("[a-zA-Z\\s0-9&._-]+")
		if (regex.matches(userName.value.orEmpty()).not()) {
			return fragment.showError(fragment.getString(R.string.nickname_must_be_in_english_characters))
		}

		if (showValidPhoneNumForAdsPhone.value.orFalse().not()
			&& advertisingPhone.value.isNullOrEmpty().not()) {
			return fragment.showError(fragment.getString(R.string.contact_phone_num_is_invalid))
		}
		if (showValidPhoneNumForWhatsAppPhone.value.orFalse().not()
			&& whatsAppPhone.value.isNullOrEmpty().not()) {
			return fragment.showError(fragment.getString(R.string.whatsapp_phone_num_is_invalid))
		}

		if (advertisingPhone.value.isNullOrEmpty().not()
			&& activatedAdvertisingPhone.value.trimAllWhitespaces() != advertisingPhone.value.trimAllWhitespaces()) {
			return fragment.showError(fragment.getString(R.string.contact_phone_num_is_not_activated))
		}
		if (whatsAppPhone.value.isNullOrEmpty().not()
			&& activatedWhatsAppPhone.value.trimAllWhitespaces() != whatsAppPhone.value.trimAllWhitespaces()) {
			return fragment.showError(fragment.getString(R.string.whatsapp_phone_num_is_not_activated))
		}

		// Both will be ignored and not sent since on verify will be auto-updated cuz backend can't do it.
		val adsPhoneCountryCode = fragment.binding.countryCodePickerForAdsPhone.selectedCountryCodeWithPlus
		val adsPhone = if (showNotActivatedAdsPhone.value == true) null else {
			fragment.binding.countryCodePickerForAdsPhone.fullNumberWithPlus.trimAllWhitespaces()
				.orNullIfNullOrEmpty()?.substringAfter(adsPhoneCountryCode).orNullIfNullOrEmpty()
		}
		val whatsAppPhoneCountryCode = fragment.binding.countryCodePickerForAdsPhone.selectedCountryCodeWithPlus
		val whatsAppPhone = if (showNotActivatedWhatsAppPhone.value == true) null else {
			fragment.binding.countryCodePickerForWhatsAppPhone.fullNumberWithPlus.trimAllWhitespaces()
				.orNullIfNullOrEmpty()?.substringAfter(whatsAppPhoneCountryCode).orNullIfNullOrEmpty()
		}

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				useCaseShop.addOrUpdateStore(
					(backgroundImage.value as? GlideImageViaXmlModel.IUri)?.uri
						?.createMultipartBodyPart(context.applicationContext, "background"),
					(profileImage.value as? GlideImageViaXmlModel.IUri)?.uri
						?.createMultipartBodyPart(context.applicationContext, "image"),
					storeName.value.orEmpty(),
					userName.value.orEmpty(),
					selectedCity.value?.id.orZero(),
					selectedArea.value?.id.orZero(),
					locationData.value?.latitude.orEmpty(),
					locationData.value?.longitude.orEmpty(),
					locationData.value?.address.orEmpty(),
					description.value.orEmpty(),
					advertisingLink.value.orEmpty(),
					email.value.orEmpty(),
					websiteLink.value.orEmpty(),
					// Both will be ignored and not sent since on verify will be auto-updated cuz backend can't do it.
					null/*if (adsPhone.isNullOrEmpty()) null else adsPhoneCountryCode*/,
					null/*adsPhone*/,
					null/*if (whatsAppPhone.isNullOrEmpty()) null else whatsAppPhoneCountryCode*/,
					null/*whatsAppPhone*/,
					taxNumber.value.orEmpty(),
				)
			}
		) {
			userLocalUseCase(userLocalUseCase().copy(isStore = true))

			fragment.apply {
				showMessage(getString(R.string.done_successfully))

				findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
					true, // created shop successfully
				)
			}
		}
	}

}
