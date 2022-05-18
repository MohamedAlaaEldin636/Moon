package grand.app.moon.helpers.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import grand.app.moon.R
import grand.app.moon.helpers.map.cluster.ClusterCustomItem
import java.io.IOException
import java.util.*

class MapConfig {
  var markers_service: List<LatLng> = ArrayList()
  var markers = ArrayList<Marker>()
  var context: Context? = null
  var mMap: GoogleMap? = null
  var driverMarker: Marker? = null
  var direction: Polyline? = null
  var distanceKm = 0.0
  var markersId = HashMap<Marker, Any>()

  constructor(context: Context?, map: GoogleMap?) {
    this.context = context
    mMap = map
  }

  constructor() {}

  fun clearMap() {
    mMap!!.clear()
  }

  var clusterManager: ClusterManager<ClusterCustomItem>? = null
  fun setUpCluster() {
    // Position the map.
    // Initialize the manager with the context and the map.
    // (Activity extends context, so we can pass 'this' in the constructor.)
    clusterManager = ClusterManager(context, mMap)
    mMap!!.setOnCameraIdleListener(clusterManager)
    mMap!!.setOnMarkerClickListener(clusterManager)
    // Add cluster items (markers) to the cluster manager.
  }

  fun setSettings() {
    mMap?.let {
      it.mapType = GoogleMap.MAP_TYPE_NORMAL
      //        mMap.setTrafficEnabled(true);
//        mMap.setBuildingsEnabled(true);
      it.isIndoorEnabled = true
      it.uiSettings.isRotateGesturesEnabled = false
      it.uiSettings.isZoomControlsEnabled = true
      it.uiSettings.isZoomGesturesEnabled = true
    }

  }

  fun setMapStyle() {
    Log.d(TAG, "setMapStyle: ")
    try {
      val style = MapStyleOptions("[" +
        "  {" +
        "    \"featureType\":\"poi.business\"," +
        "    \"elementType\":\"all\"," +
        "    \"stylers\":[" +
        "      {" +
        "        \"visibility\":\"off\"" +
        "      }" +
        "    ]" +
        "  }," +
        "  {" +
        "    \"featureType\":\"transit\"," +
        "    \"elementType\":\"all\"," +
        "    \"stylers\":[" +
        "      {" +
        "        \"visibility\":\"off\"" +
        "      }" +
        "    ]" +
        "  }" +
        "]");
      val isSuccess =
        mMap!!.setMapStyle(context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_style) })
//      mMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID;
      if (!isSuccess) Log.e("errorMapStyle", "errorMap")
      else Log.d(TAG, "errorMapStyle: not found ")
    } catch (ex: Resources.NotFoundException) {
      Log.d(TAG, "setMapStyle: ${ex.message}")
      ex.printStackTrace()
    }
  }

  fun moveCamera(latLng: LatLng?) {
    if (isVisible) latLng?.let { CameraUpdateFactory.newLatLngZoom(it, 15.0f) }?.let {
      mMap!!.animateCamera(
        it
      )
    }
  }

  fun moveCamera(latLngs: ArrayList<LatLng>) {
    if (latLngs.size > 0 && isVisible) {
      val builder = LatLngBounds.Builder()
      for (latLng in latLngs) {
        if (latLng != null) {
          builder.include(latLng)
        }
      }
      //            int padding = 400; // offset from edges of the map in pixels
      val padding = 100 // offset from edges of the map in pixels
      val bounds = builder.build()
      val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
      /**call the map call back to know map is loaded or not */
      mMap!!.setOnMapLoadedCallback {
        /**set animated zoom camera into map */
        /**set animated zoom camera into map */
        mMap!!.animateCamera(cu)
      }
    }
  }

  fun setRoute(direction: Polyline?) {
    this.direction = direction
  }

  fun removeRoute() {
    if (direction != null) direction!!.remove()
  }


  fun addMarker(latLng: LatLng?, icon: Int, title: String?): Marker? {
    val marker = latLng?.let {
      MarkerOptions().position(it)
        .icon(BitmapDescriptorFactory.fromResource(icon))
        .title(title)
    }?.let {
      mMap!!.addMarker(
        it
      )
    }
    if (marker != null) {
      markers.add(marker)
    }
    return marker
  }

  fun addMarker(latLng: LatLng?, icon: Int, title: String?, snipped: String?): Marker? {
    val marker = latLng?.let {
      MarkerOptions().position(it)
        .icon(BitmapDescriptorFactory.fromResource(icon))
        .title(title)
        .snippet(snipped)
    }?.let {
      mMap!!.addMarker(
        it
      )
    }
    if (marker != null) {
      markers.add(marker)
    }
    return marker
  }

  fun clearMarkers() {
    markers.clear()
  }

  fun changeMyLocationButtonLocation(mapView: MapView?) {
    if (mapView?.findViewById<View?>("1".toInt()) != null
    ) {
      // Get the button view
      val locationButton =
        (mapView.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
      // and next place it, on bottom right (as Google Maps app)
      val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
      // position on right bottom
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
      //            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
      layoutParams.setMargins(0, 0, 0, 0)
    }
  }

  fun getGoogleMap(): GoogleMap? {
    return mMap
  }

  fun clear() {
    clusterManager?.clearItems()
    markersId.clear()
    markers.clear()
    mMap!!.clear()
  }

  fun zoomCamera(padding: Int): LatLngBounds {
    val builder = LatLngBounds.Builder()
    for (marker in markers) {
      builder.include(marker.position)
    }
    val bounds = builder.build()
    val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
    mMap!!.animateCamera(cu)
    return bounds
  }

  fun navigation(lat: Double, lng: Double) {
    val intent = Intent(
      Intent.ACTION_VIEW,
      Uri.parse("http://maps.google.com/maps?daddr=$lat,$lng")
    )
    context!!.startActivity(intent)
  }

  fun navigation(latLngPicker: LatLng, latLngDestination: LatLng) {
    val intent = Intent(
      Intent.ACTION_VIEW,
      Uri.parse("http://maps.google.com/maps?saddr=" + latLngPicker.latitude + "," + latLngPicker.longitude + "&daddr=" + latLngDestination.latitude + "," + latLngDestination.longitude + "")
    )
    context!!.startActivity(intent)
  }


  private var mLocationRequest: LocationRequest? = null
  fun createLocationRequest() {
    mLocationRequest = LocationRequest()
    mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
    mLocationRequest!!.fastestInterval = FATEST_INTERVAL.toLong()
    mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest!!.smallestDisplacement = DISPLACEMENT.toFloat()
  }

  fun getmLocationRequest(): LocationRequest? {
    if (mLocationRequest == null) {
      createLocationRequest()
    }
    return mLocationRequest
  }

  fun setmLocationRequest(mLocationRequest: LocationRequest?) {
    this.mLocationRequest = mLocationRequest
  }


  val isVisible: Boolean
    get() = if (context == null || mMap == null && clusterManager != null) false else true

  fun destroy() {
    clusterManager?.clearItems()
    if (mMap != null) mMap!!.clear()
    context = null
    mMap = null
  }

  var counter = 0
  var markersData = ArrayList<MarkerOptions>()
  fun getId(marker: Marker): Any? {
    return markersId[marker]
  }


  companion object {
    private const val TAG = "MapConfig"
    private const val UPDATE_INTERVAL = 5000 * 60
    private const val FATEST_INTERVAL = 3000
    private const val DISPLACEMENT = 10
  }
}
