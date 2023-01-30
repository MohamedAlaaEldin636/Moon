package grand.app.moon.presentation.myAds

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import grand.app.moon.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentLocationSelectionBinding
import grand.app.moon.extensions.LocationHandler
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.model.getAddressFromLatitudeAndLongitude
import grand.app.moon.presentation.myAds.viewModel.LocationSelectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

@AndroidEntryPoint
class LocationSelectionFragment : BaseFragment<FragmentLocationSelectionBinding>(),
    OnMapReadyCallback, LocationHandler.Listener {

    companion object {
        const val KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON = "KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON"
    }

    private val viewModel by viewModels<LocationSelectionViewModel>()

    private lateinit var locationHandler: LocationHandler

    /** Zoom levels https://developers.google.com/maps/documentation/android-sdk/views#zoom */
    val zoom get() = min(viewModel.googleMap?.maxZoomLevel ?: 5f, 15f)

    var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            requireContext(),
            this
        )

        super.onCreate(savedInstanceState)

        if (!viewModel.args.showBackButton) {
            onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this, true) {
                // Do Nothing.
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_location_selection

		override fun setBindingVariables() {
			binding?.viewModel = viewModel
		}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
	      super.onViewCreated(view, savedInstanceState)

        // Setup map
        (childFragmentManager.findFragmentById(R.id.mapFragmentContainerView) as? SupportMapFragment)
            ?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.googleMap = googleMap

	    viewModel.googleMap?.setOnCameraIdleListener {
				lifecycleScope.launch(Dispatchers.IO) {
					val googleMap2 = viewModel.googleMap ?: return@launch

					context?.getAddressFromLatitudeAndLongitude(
						withContext(Dispatchers.Main) {
							googleMap2.cameraPosition.target.latitude
						},
						withContext(Dispatchers.Main) {
							googleMap2.cameraPosition.target.longitude
						}
					)?.also {
						withContext(Dispatchers.Main) {
							viewModel.search.value = it
						}
					}
				}
	    }

        MyLogger.e("onMapReady -> ${viewModel.args.latitude}")

        if (viewModel.args.latitude == null || viewModel.args.longitude == null) {
            moveToCurrentLocation()
        }

        val location = LatLng(
            viewModel.args.latitude?.toDouble() ?: 0.0,
            viewModel.args.longitude?.toDouble() ?: 0.0
        )

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                viewModel.resultOfPlaceFragment.value ?: location,
                zoom
            )
        )
    }

    fun moveToCurrentLocation() {
        viewModel.myCurrentLocation?.also { myCurrentLocation ->
            viewModel.googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(myCurrentLocation, zoom)
            )
        } ?: locationHandler.requestCurrentLocation(true)
    }

    override fun onCurrentLocationResultSuccess(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)

        viewModel.myCurrentLocation = latLng

        viewModel.googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

}
