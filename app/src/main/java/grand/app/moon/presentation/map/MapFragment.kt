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
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.map.viewModel.MapViewModel
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {
  private val viewModel: MapViewModel by viewModels()
  var map : SupportMapFragment? = null
//  val args: MapFragmentArgs by navArgs()

  override
  fun getLayoutId() = R.layout.fragment_map

  override
  fun setBindingVariables() {
    viewModel.categoryItem.name = resources.getString(R.string.all)
    binding.viewModel = viewModel
//    if(arguments!= null && requireArguments().containsKey(Constants.TYPE))
//      viewModel.type = requireArguments().getString(Constants.TYPE).toString()
    if(arguments != null) {
      // it means ads
      val args: MapFragmentArgs by navArgs()
      viewModel.type = args.type
      viewModel.categoryId = args.categoryId
      viewModel.subCategoryId = args.subCategoryId
      viewModel.propertyId = args.propertyId
      viewModel.setSubCategories(args.subCategory)
    }else
      viewModel.getCategories() // store

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    map = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
    map?.getMapAsync(this)
    super.onViewCreated(view, savedInstanceState)
  }

  override fun setupObservers() {

    viewModel.categoriesAdapter.clickEvent.observe(this, { category ->
      viewModel.stores.clear()
      if(category.id == -1){
        viewModel.stores.addAll(viewModel.backUp)
      }else {
        viewModel.backUp.forEach { store ->
          when(viewModel.type){
            Constants.ADVERTISEMENT_TEXT -> {
              Log.d(TAG, "setupObservers SubCategory: ${store.subCategoryId}")
              Log.d(TAG, "setupObservers CategoryId: ${category.id}")
              if(store.subCategoryId == category.id)
                viewModel.stores.add(store)
            }
            else -> {
              store.category.forEach { categoryItem ->
                if (categoryItem.id == category.id)
                  viewModel.stores.add(store)
              }
            }
          }

        }
      }
      Log.d(TAG, "setupObservers: ${viewModel.stores.size}")
      loadMarkers()
    })

    lifecycleScope.launchWhenResumed {
      viewModel.responseStores.collect {
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

    lifecycleScope.launchWhenResumed {
      viewModel.responseAds.collect {
        Log.d(TAG, "setupObservers: HERE")
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.setAdsData(it.value.data)
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

    viewModel.mapConfig?.clear()
    viewModel.mapConfig?.clusterManager?.cluster()
    viewModel.stores.forEachIndexed { index, store ->
      val clusterItem = ClusterCustomItem(store, index)
      viewModel.mapConfig?.clusterManager?.addItem(clusterItem)
      viewModel.clusterCustomItems.add(clusterItem)
      viewModel.latLngs.add(clusterItem.position)
    }


    viewModel.markerRender = viewModel.mapConfig?.let { MarkerRender(it, viewModel.clusterCustomItems) }
    viewModel.mapConfig?.clusterManager?.renderer = viewModel.markerRender
////                                viewModel.mapConfig.clusterManager?.setOnClusterClickListener(this);
////                                viewModel.mapConfig.clusterManager?.setOnClusterItemClickListener(this);
//      //                                viewModel.mapConfig.clusterManager?.setOnClusterClickListener(this);
////                                viewModel.mapConfig.clusterManager?.setOnClusterItemClickListener(this);
    viewModel.mapConfig?.getGoogleMap()?.let {
      it.setOnCameraIdleListener(viewModel.mapConfig?.clusterManager)
      it.setOnMarkerClickListener(viewModel.mapConfig?.clusterManager)
      it.setOnInfoWindowClickListener(viewModel.mapConfig?.clusterManager)

      viewModel.mapConfig?.clusterManager?.cluster()
      viewModel.mapConfig?.moveCamera(viewModel.latLngs)

      it.setOnMarkerClickListener(
        GoogleMap.OnMarkerClickListener { marker ->
          Log.d(TAG, "loadMarkers: ${marker.id}")
          Log.d(TAG, "loadMarkers: ${marker.tag}")
//          Toast.makeText(context, "HERE", Toast.LENGTH_SHORT).show()
          marker.showInfoWindow()
          true
        })
      it.setOnMapClickListener { latLng -> }
      viewModel.mapConfig?.clusterManager?.setOnClusterItemClickListener(
        OnClusterItemClickListener<ClusterCustomItem?> {
          try {
            viewModel.mapConfig?.clear()
            viewModel.mapConfig?.clusterManager?.clearItems()
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

  private val TAG = "MapFragment"
  override fun onMapReady(p0: GoogleMap) {
    Log.d(TAG, "onMapReady: ")
    viewModel.mapConfig = MapConfig(requireContext(), p0)
    viewModel.mapConfig!!.setMapStyle() //set style google map
//    //cluster manager
    viewModel.mapConfig!!.setUpCluster()
    viewModel.callService()

  }

  override fun onStart() {
    super.onStart()
    map?.onStart()
  }

  override fun onPause() {
    super.onPause()
    map?.onPause()

  }


  override fun onStop() {
    super.onStop()
    map?.onStop()

  }


  override fun onLowMemory() {
    super.onLowMemory()
    map?.onLowMemory()
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.mapConfig?.getGoogleMap()?.clear()
  }
}