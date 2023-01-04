package grand.app.moon.presentation.map

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.*
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentMapBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.orZero
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.helpers.map.cluster.ClusterCustomItem
import grand.app.moon.helpers.map.cluster.MarkerRender
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.map.viewModel.MapViewModel
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {
  val viewModel: MapViewModel by viewModels()
  var map: SupportMapFragment? = null
//  val args: MapFragmentArgs by navArgs()

  override
  fun getLayoutId() = R.layout.fragment_map

  override
  fun setBindingVariables() {
    viewModel.categoryItem.name = resources.getString(R.string.all)
    binding.viewModel = viewModel
//    if(arguments!= null && requireArguments().containsKey(Constants.TYPE))
//      viewModel.type = requireArguments().getString(Constants.TYPE).toString()
    when(arguments){
      null -> {
        viewModel.getCategories()
      }
      else -> {
        if(requireArguments().containsKey("order_by")){
          viewModel.getCategories()
        }else if(requireArguments().containsKey("type")) {
          val args: MapFragmentArgs by navArgs()
          viewModel.type = args.type
          viewModel.categoryId = args.categoryId
          viewModel.subCategoryId = args.subCategoryId
          viewModel.propertyId = args.propertyId
          viewModel.setSubCategories(args.subCategory)
        }
      }
    }

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    map = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
    map?.getMapAsync(this)
    super.onViewCreated(view, savedInstanceState)
  }

  override fun setupObservers() {

    viewModel.categoriesAdapter.clickEvent.observe(this, { category ->
      viewModel.stores.clear()
      when (viewModel.type) {
        Constants.ADVERTISEMENT_TEXT -> {
          viewModel.propertyId = category.id.toString()
        }
        else -> {
          viewModel.categoryId = category.id.toString()
        }
      }
      viewModel.showAdvertisement.set(false)
      viewModel.callService()
      Log.d(TAG, "setupObservers: ${viewModel.stores.size}")
      loadMarkers()
    })
    viewModel.subCategoriesAdapter.clickEvent.observe(this, { category ->
      Log.d(TAG, "setupObservers: ${viewModel.subCategoriesAdapter.selected}")
      viewModel.stores.clear()
      Log.d(TAG, "setupObservers: ${viewModel.subCategoryId}")
      when (viewModel.type) {
        Constants.ADVERTISEMENT_TEXT -> {
          viewModel.propertyId = category.id.toString()
        }
        else -> {
          viewModel.subCategoryId = category.id.toString()
        }
      }
      if(viewModel.subCategoriesAdapter.selected != 0)
        viewModel.subCategoryId = viewModel.subCategoriesAdapter.differ.currentList[viewModel.subCategoriesAdapter.selected].id.toString()
      else
        viewModel.subCategoryId = null
      viewModel.showAdvertisement.set(false)
      viewModel.callService()
//      Log.d(TAG, "setupObservers: ${viewModel.stores.size}")
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
	        else -> {}
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
	        else -> {}
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


    viewModel.markerRender =
      viewModel.mapConfig?.let { MarkerRender(it, viewModel.clusterCustomItems) }
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
          if (marker.tag != null) {
            try {
              when (viewModel.type) {
                Constants.ADVERTISEMENT_TEXT -> {
                  viewModel.findAds(marker.tag.toString()?.toIntOrNull().orZero())
                }
                else -> {
                  findNavController().navigate(
                    R.id.nav_store,
                    bundleOf(
                      "id" to marker.tag.toString()?.toInt(),
                      "type" to 3
                    ), Constants.NAVIGATION_OPTIONS
                  )
                }

              }
            }   catch(e: Exception) {
              e.printStackTrace()
            }
          }
          marker.showInfoWindow()
          true
        })
      it.setOnMapClickListener { latLng ->
        viewModel.showAdvertisement.set(false)
      }
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

  var isLoaded = false
  private val TAG = "MapFragment"
  override fun onMapReady(p0: GoogleMap) {
    Log.d(TAG, "onMapReady: ")
    if (!isLoaded) {
      viewModel.mapConfig = MapConfig(requireContext(), p0)
      viewModel.mapConfig!!.setMapStyle() //set style google map
//    //cluster manager
      viewModel.mapConfig!!.setUpCluster()
      isLoaded = true
      onResume()
    }

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

  override fun onResume() {
    super.onResume()
    if(isLoaded){
      viewModel.callService()
    }
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