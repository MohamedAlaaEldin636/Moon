package grand.app.moon.presentation.myAds.viewModel

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService
import androidx.core.view.postDelayed
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
import grand.app.moon.extensions.firstChildInstance
import grand.app.moon.extensions.toJsonInlinedOrNull
import grand.app.moon.presentation.myAds.LocationSelectionFragment
import grand.app.moon.presentation.myAds.LocationSelectionFragmentArgs
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myAds.model.getAddressFromLatitudeAndLongitude
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LocationSelectionViewModel @Inject constructor(
	val args: LocationSelectionFragmentArgs,
) : ViewModel() {

    val showBackButton = args.showBackButton

    val search = MutableLiveData("")

    val showMapNotSearchResults = MutableLiveData(true)

    val resultOfPlaceFragment = MutableLiveData<LatLng>()

    var myCurrentLocation: LatLng? = null

    var googleMap: GoogleMap? = null

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

    fun toSearchPlace(view: View) {
        showMapNotSearchResults.value = false

        val fragment = view.findFragment<LocationSelectionFragment>()

        if (!Places.isInitialized()) {
            Places.initialize(view.context.applicationContext, view.context.getString(R.string.google_geo_api_key))
        }

        val autocompleteSupportFragment = (fragment.childFragmentManager
            .findFragmentById(R.id.placesFragmentContainerView) as AutocompleteSupportFragment)

        autocompleteSupportFragment.setPlaceFields(listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG
        ))

        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                MyLogger.e("dosdkodks Places API selected place ${place.latLng}")

                resultOfPlaceFragment.postValue(place.latLng)

                showMapNotSearchResults.postValue(true)

                fragment.binding?.root?.post {
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            place.latLng ?: return@post,
                            fragment.zoom
                        )
                    )
                }
            }

            override fun onError(status: Status) {
                MyLogger.e("Places API error with status $status")

                fragment.requireContext().showError(fragment.getString(R.string.something_went_wrong_please_try_again))

                showMapNotSearchResults.postValue(true)
            }
        })

      val editText = (autocompleteSupportFragment.view as? ViewGroup)?.firstChildInstance<EditText>()
	    editText?.performClick()
    }

    fun moveToCurrentLocation(view: View) {
        view.findFragment<LocationSelectionFragment>().moveToCurrentLocation()
    }

    fun onSelectLocationClick(view: View) {
        val fragment = view.findFragment<LocationSelectionFragment>()

        val googleMap = googleMap ?: return

        fragment.showLoading()

        fragment.lifecycleScope.launch {
            val address = fragment.requireContext().getAddressFromLatitudeAndLongitude(
                googleMap.cameraPosition.target.latitude,
                googleMap.cameraPosition.target.longitude
            )

            val locationData = LocationData(
                googleMap.cameraPosition.target.latitude.toString(),
                googleMap.cameraPosition.target.longitude.toString(),
                address
            )

            fragment.hideLoading()

            delay(300)

            fragment.onBackPressedCallback?.isEnabled = false

            val navController = fragment.findNavController()

            navController.navigateUp()

            navController.currentBackStackEntry?.savedStateHandle?.set(
                LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
                locationData.toJsonInlinedOrNull()
            )
        }
    }

}
