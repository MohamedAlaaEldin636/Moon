package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.myStore.ResponseMyStoreDetails
import grand.app.moon.extensions.compose.GlideImageViaXmlModel
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.navUpThenSetResultInBackStackEntrySavedStateHandleViaGson
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myStore.CreateStoreFragment
import javax.inject.Inject

/**
 * todo nor update nor get info exists only create.
 *  lesa until sprint 3.
 *
 *  todo kfaaya astrix for required and sheeel kol el aksam isa.
 *
 *   returns null ...
 */
@HiltViewModel
class CreateStoreViewModel @Inject constructor(
	application: Application,
) : AndroidViewModel(application) {

	val response = MutableLiveData<ResponseMyStoreDetails>()

	val backgroundImage = MutableLiveData<GlideImageViaXmlModel>()

	val profileImage = MutableLiveData<GlideImageViaXmlModel>()

	val storeName = MutableLiveData("")

	val userName = MutableLiveData("")

	val cities = emptyList<Country>()
	val selectedCity = MutableLiveData<Country>()

	val areas = emptyList<Country>()
	val selectedArea = MutableLiveData<Country>()

	private val locationData = MutableLiveData<LocationData?>()
	val address = locationData.map { it?.address }

	val description = MutableLiveData("")

	fun createOrUpdateStore(view: View) {
		// todo Fields checks

		val fragment = view.findFragmentOrNull<CreateStoreFragment>() ?: return

		fragment.apply {
			showMessage(getString(R.string.done_successfully))

			findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
				true, // created shop successfully
			)
		}
	}

}
