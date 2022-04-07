package grand.app.moon.presentation.map

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.*
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentMapBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.helpers.map.cluster.ClusterCustomItem
import grand.app.moon.helpers.map.cluster.MarkerRender
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>() , OnMapReadyCallback{
  val args: MapFragmentArgs by navArgs()
  private val viewModel: MapViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_map

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.type = args.type
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    (childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment)
      ?.getMapAsync(this)
    super.onViewCreated(view, savedInstanceState)
  }
  override fun setupObservers() {

    lifecycleScope.launchWhenResumed {
      viewModel.response.collect {
        Log.d(TAG, "setupObservers: HERE")
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.setData(it.value.data)
            loadMarkers()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }

  private fun loadMarkers() {
    viewModel.mapConfig.clear()
    viewModel.mapConfig.clusterManager?.cluster()
    viewModel.stores.forEachIndexed { index, store ->
      val clusterItem = ClusterCustomItem(store, index)
      viewModel.mapConfig.clusterManager?.addItem(clusterItem)
      viewModel.clusterCustomItems.add(clusterItem)
      viewModel.latLngs.add(clusterItem.position)
    }


      viewModel.markerRender = MarkerRender(viewModel.mapConfig, viewModel.clusterCustomItems)
    viewModel.mapConfig.clusterManager?.renderer = viewModel.markerRender
////                                viewModel.mapConfig.clusterManager?.setOnClusterClickListener(this);
////                                viewModel.mapConfig.clusterManager?.setOnClusterItemClickListener(this);
//      //                                viewModel.mapConfig.clusterManager?.setOnClusterClickListener(this);
////                                viewModel.mapConfig.clusterManager?.setOnClusterItemClickListener(this);
      viewModel.mapConfig.getGoogleMap()?.let {
        it.setOnCameraIdleListener(viewModel.mapConfig.clusterManager)
        it.setOnMarkerClickListener(viewModel.mapConfig.clusterManager)
        it.setOnInfoWindowClickListener(viewModel.mapConfig.clusterManager)

        viewModel.mapConfig.clusterManager?.cluster()
        viewModel.mapConfig.moveCamera(viewModel.latLngs)

        it.setOnMarkerClickListener(
          GoogleMap.OnMarkerClickListener { marker ->
            marker.showInfoWindow()
            true
          })
        it.setOnMapClickListener { latLng -> }
        viewModel.mapConfig.clusterManager?.setOnClusterItemClickListener(
          OnClusterItemClickListener<ClusterCustomItem?> {
            try {
              viewModel.mapConfig.clear()
              viewModel.mapConfig.clusterManager?.clearItems()
              //                                            viewModel.mapConfig.getGoogleMap().animateCamera(CameraUpdateFactory
              //                                                    .newLatLngZoom(placesCluster.getPosition(),
              //                                                            viewModel.mapConfig.getGoogleMap().getCameraPosition().zoom));
              //                                            setItemChecked(placesCluster);
            } catch (exception: Exception) {
              exception.printStackTrace()
            }
            true
          })
      }
  }

  private  val TAG = "MapFragment"
  override fun onMapReady(p0: GoogleMap) {
    Log.d(TAG, "onMapReady: ")
    viewModel.mapConfig = MapConfig(requireContext(), p0)
    viewModel.mapConfig.setMapStyle() //set style google map
//    //cluster manager
    viewModel.mapConfig.setUpCluster()
    viewModel.callService()

  }
}