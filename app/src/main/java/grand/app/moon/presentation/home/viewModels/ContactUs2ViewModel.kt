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
import grand.app.moon.databinding.ItemContactUsBinding
import grand.app.moon.databinding.ItemInsideItemContactUsBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.ComplainsAndSuggestionsFragment
import grand.app.moon.presentation.home.ContactUs2Fragment
import grand.app.moon.presentation.home.models.ItemInsideContactUsData
import grand.app.moon.presentation.home.models.ResponseContactUsData
import grand.app.moon.presentation.home.models.ResponseSettings
import javax.inject.Inject

@HiltViewModel
class ContactUs2ViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	var response = emptyList<ResponseContactUsData>()

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
		uri?.let { app.getString(R.string.added_successfully) }.orEmpty()
	}

	val message = MutableLiveData("")

	val selectedTypeOfData = MutableLiveData<ContactUs2Fragment.Selection?>()

	val adapter = RVItemCommonListUsage<ItemContactUsBinding, ResponseContactUsData>(
		R.layout.item_contact_us
	) { binding, _, item ->
		binding.imageView.setImageResource(
			when (selectedTypeOfData.value) {
				ContactUs2Fragment.Selection.EMAIL -> R.drawable.contact_us_msg
				ContactUs2Fragment.Selection.PHONE -> R.drawable.contact_us_call
				ContactUs2Fragment.Selection.LOCATION -> R.drawable.contact_us_location
				null -> 0
			}
		)

		binding.nameTextView.text = item.country

		binding.recyclerView.setupWithRVItemCommonListUsage(
			getInnerAdapter().also { innerAdapter ->
				val list = when (selectedTypeOfData.value) {
					ContactUs2Fragment.Selection.EMAIL -> response.flatMap {
						it.emails.orEmpty()
					}
					ContactUs2Fragment.Selection.PHONE -> response.flatMap {
						it.phones.orEmpty()
					}
					ContactUs2Fragment.Selection.LOCATION -> response.flatMap {
						it.addresses.orEmpty()
					}
					null -> emptyList()
				}

				innerAdapter.submitList(list)
			},
			false,
			1
		)
	}

	private fun getInnerAdapter() = RVItemCommonListUsage<ItemInsideItemContactUsBinding, ItemInsideContactUsData>(
		R.layout.item_inside_item_contact_us,
		onItemClick = { _, binding ->
			val fragment = binding.root.findFragmentOrNull<ContactUs2Fragment>() ?: return@RVItemCommonListUsage
			val context = fragment.context ?: return@RVItemCommonListUsage
			val value = binding.textView.text.toStringOrEmpty()
			when (selectedTypeOfData.value) {
				ContactUs2Fragment.Selection.EMAIL -> {
					context.launchSendEMail(value, context.getString(R.string.contact_us))
				}
				ContactUs2Fragment.Selection.PHONE -> {
					context.launchDialNumber(value)
				}
				ContactUs2Fragment.Selection.LOCATION -> {}
				null -> {}
			}
		}
	) { binding, _, item ->
		binding.textView.text = item.content
	}

	fun changeSelectedTypeOfData(selectedTypeOfData: ContactUs2Fragment.Selection) {
		this.selectedTypeOfData.value = selectedTypeOfData
	}

	fun pickType(view: View) {
		val fragment = view.findFragmentOrNull<ContactUs2Fragment>() ?: return

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
		val fragment = view.findFragmentOrNull<ContactUs2Fragment>() ?: return

		fragment.gettingImageHandler?.requestImageOrVideo()
	}

	fun send(view: View) {
		val fragment = view.findFragmentOrNull<ContactUs2Fragment>() ?: return

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
				repoShop.setContactUsSettings(
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

	private fun ContactUs2Fragment.getAllTypes(onSuccess: () -> Unit) {
		handleRetryAbleActionCancellable(
			action = {
				repoShop.getContactUsTypes()
			}
		) {
			allTypes = it

			onSuccess()
		}
	}

}
