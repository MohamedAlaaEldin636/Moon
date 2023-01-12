package grand.app.moon.presentation.myAds.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.toJsonInlinedOrNull
import grand.app.moon.presentation.myAds.LocationSelectionFragment
import grand.app.moon.presentation.myAds.LocationSelectionFragmentArgs
import grand.app.moon.presentation.myAds.LocationViewerFragment
import grand.app.moon.presentation.myAds.LocationViewerFragmentArgs
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myAds.model.getAddressFromLatitudeAndLongitude
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LocationViewerViewModel @Inject constructor(
	val args: LocationViewerFragmentArgs,
) : ViewModel() {

    var myCurrentLocation: LatLng? = null

    var googleMap: GoogleMap? = null

    fun moveToCurrentLocation(view: View) {
        view.findFragment<LocationViewerFragment>().moveToCurrentLocation()
    }

}
