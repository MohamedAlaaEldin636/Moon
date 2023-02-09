package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.ComplainsAndSuggestionsFragment
import grand.app.moon.presentation.home.models.ResponseSettings
import javax.inject.Inject

@HiltViewModel
class ComplainsAndSuggestionsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val name = MutableLiveData("")

	val phone = MutableLiveData("")

	val showValidPhoneNum = MutableLiveData(false)

	private var allTypes = emptyList<ResponseSettings>()
	private val selectedType = MutableLiveData<ResponseSettings?>()
	val type = selectedType.map { it?.content.orEmpty() }

	val showImage = selectedType.map {
		it?.hasImage.orFalse()
	}
	val image = MutableLiveData<Uri?>()
	val textImage = image.map { uri ->
		uri?.let { app.getString(R.string.image_has_been_added) }.orEmpty()
	}

	val message = MutableLiveData("")

	fun pickType(view: View) {
		val fragment = view.findFragmentOrNull<ComplainsAndSuggestionsFragment>() ?: return

		val onSuccess = {
			view.showPopup(
				allTypes.map { it.content.orEmpty() },
				listener = { menuItem ->
					selectedType.value = allTypes.firstOrNull { it.content == menuItem.title }
				}
			)
		}

		if (allTypes.isEmpty()) {
			fragment.getAllTypes {
				onSuccess()
			}
		}else {
			onSuccess()
		}
	}

	fun pickImage(view: View) {
		val fragment = view.findFragmentOrNull<ComplainsAndSuggestionsFragment>() ?: return

		fragment.gettingImageHandler?.requestImageOrVideo()
	}

	fun send(view: View) {
		val fragment = view.findFragmentOrNull<ComplainsAndSuggestionsFragment>() ?: return

		val context = fragment.context ?: return

		if (name.value.isNullOrEmpty() || selectedType.value == null || message.value.isNullOrEmpty()
			|| phone.value.isNullOrEmpty() || (selectedType.value?.hasImage == true && image.value == null)) {
			return fragment.showError(fragment.getString(R.string.all_fields_required))
		}

		if (showValidPhoneNum.value.orFalse().not()) {
			return fragment.showError(fragment.getString(R.string.phone_num_is_invalid))
		}

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.setComplainsAndSuggestionsSettings(
					name.value.orEmpty(),
					selectedType.value?.id.orZero(),
					message.value.orEmpty(),
					fragment.binding.countryCodePicker.fullNumberWithPlus,
					image.value?.createMultipartBodyPart(context, "file")
				)
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			fragment.findNavController().navigateUp()
		}
	}

	private fun ComplainsAndSuggestionsFragment.getAllTypes(onSuccess: () -> Unit) {
		handleRetryAbleActionCancellable(
			action = {
				repoShop.getComplainsAndSuggestionsTypes()
			}
		) {
			allTypes = it

			onSuccess()
		}
	}

}
