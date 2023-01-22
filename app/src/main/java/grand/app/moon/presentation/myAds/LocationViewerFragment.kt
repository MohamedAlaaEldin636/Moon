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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import grand.app.moon.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentLocationSelectionBinding
import grand.app.moon.databinding.FragmentLocationViewerBinding
import grand.app.moon.extensions.LocationHandler
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.LocationSelectionViewModel
import grand.app.moon.presentation.myAds.viewModel.LocationViewerViewModel
import kotlin.math.min

@AndroidEntryPoint
class LocationViewerFragment : BaseFragment<FragmentLocationViewerBinding>(),
    OnMapReadyCallback, LocationHandler.Listener {

    private val viewModel by viewModels<LocationViewerViewModel>()

	private lateinit var locationHandler: LocationHandler

    /** Zoom levels https://developers.google.com/maps/documentation/android-sdk/views#zoom */
    private val zoom get() = min(viewModel.googleMap?.maxZoomLevel ?: 5f, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            requireContext(),
            this
        )

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_location_viewer

		override fun setBindingVariables() {
			binding.viewModel = viewModel
		}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
	      super.onViewCreated(view, savedInstanceState)

        // Setup map
        (childFragmentManager.findFragmentById(R.id.mapFragmentContainerView) as? SupportMapFragment)
            ?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.googleMap = googleMap

        MyLogger.e("onMapReady -> ${viewModel.args.latitude}")

        /*if (viewModel.args.latitude == null || viewModel.args.longitude == null) {
            moveToCurrentLocation()
        }*/

        val location = LatLng(
            viewModel.args.latitude.toDoubleOrNull() ?: 0.0,
            viewModel.args.longitude.toDoubleOrNull() ?: 0.0
        )

		    googleMap.addMarker(
			    MarkerOptions().position(location)
				)

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                location,
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
