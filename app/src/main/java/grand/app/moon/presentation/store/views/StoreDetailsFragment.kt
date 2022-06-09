package grand.app.moon.presentation.store.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.tabs.TabLayout
import com.maproductions.mohamedalaa.shared.core.extensions.actOnGetIfNotInitialValueOrGetLiveData
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.databinding.FragmentStoreDetailsBinding
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.home.models.Property
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.ExploreFragmentDirections
import grand.app.moon.presentation.store.viewModels.StoreDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class StoreDetailsFragment : BaseFragment<FragmentStoreDetailsBinding>(), OnMapReadyCallback {

  private val adsDetailsFragmentArgs: StoreDetailsFragmentArgs by navArgs()
  var map: SupportMapFragment? = null
  val viewModel: StoreDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_store_details

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
      if (bundle.containsKey(Constants.SORT_BY)) {
        viewModel.setSortAds(bundle.getInt(Constants.SORT_BY))
      }
      if(bundle.containsKey(Constants.EXPLORES)) {
        Log.d(TAG, "onCreate: HWERE")
        val result = bundle.getSerializable(Constants.EXPLORES) as ExploreListPaginateData
        viewModel.exploreAdapter.updateExplores(result)
      }

    }

    actOnGetIfNotInitialValueOrGetLiveData(
      Constants.STORES_BLOCKED,
      false,
      viewLifecycleOwner,
      { it == true }
    ) {
      viewModel.blockStore = true
      viewModel.store.get()?.id?.let { ListHelper.addToBlock(it) }
      Log.d(TAG, "onViewCreated: BEFORE NAVIGATE ")
      findNavController().navigateUp()
    }

    actOnGetIfNotInitialValueOrGetLiveData(
      Constants.REVIEW,
      false,
      viewLifecycleOwner,
      { it == true }
    ) {
      Log.d(TAG, "onViewCreated: REVIEW")
      viewModel.store.get()!!.rateCount = viewModel.store.get()!!.rateCount.toInt().plus(1).toString()
      viewModel.notifyChange()
    }
  }

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }


  val days = arrayListOf<String>()

  override
  fun setupObservers() {
    days.add(resources.getString(R.string.saturday))
    days.add(resources.getString(R.string.sunday))
    days.add(resources.getString(R.string.monday))
    days.add(resources.getString(R.string.tuesday))
    days.add(resources.getString(R.string.wednesday))
    days.add(resources.getString(R.string.thursday))
    days.add(resources.getString(R.string.friday))

    viewModel.clickEvent.observe(this, {
      Log.d(TAG, "setupObservers: ${it}")
      when (it) {
//        Constants.LOGIN_REQUIRED -> openLoginActivity()
        Constants.SCROLL_DOWN -> scrollDown()
        Constants.SHARE_IMAGE -> {
          Log.d(TAG, "setupObservers: hrer")
          viewModel.share(binding.imageSlider)
        }
        Constants.GRID_1, Constants.GRID_2 -> {
          viewModel.adsAdapter.notifyDataSetChanged()
        }
      }
    })
    lifecycleScope.launchWhenResumed {
      viewModel.storeDetailsResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            updateMap()
            it.value.data.category.add(
              0,
              Property(0, name = resources.getString(R.string.show_all), parent = null)
            )
            viewModel.update(
              resources.getString(R.string.google_direction_api),
              it.value.data,
              days
            )
            if(it.value.data.explore.size == 0)
              binding.tabLayout.visibility = View.GONE
            setLocation()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return when (viewModel.adsAdapter.grid) {
          Constants.GRID_1 -> 2
          else -> 1
        }
      }
    }

    binding.rvAds.layoutManager = gridLayoutManager
    binding.rvAds.adapter = viewModel.adsAdapter
  }

  fun scrollDown() {
    binding.scrollStoreDetails.scrollTo(0, binding.rvAds.y.toInt())
//    binding.scrollStoreDetails.fullScroll(View.FOCUS_DOWN)
  }

  override fun setUpViews() {
    super.setUpViews()
//    setRecyclerViewScrollListener()
    binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

      override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
          0 -> viewModel.showAds()
          else -> viewModel.showGallery()
        }
      }

      override fun onTabReselected(tab: TabLayout.Tab?) {
        // Handle tab reselect
      }

      override fun onTabUnselected(tab: TabLayout.Tab?) {
        // Handle tab unselect
      }
    })
  }

  private fun updateMap() {
    map = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment

    map?.getMapAsync(this)
  }

  private val TAG = "StoreDetailsFragment"
  override fun onResume() {
    super.onResume()
    Log.d(TAG, "token: ${viewModel.userLocalUseCase.getKey(Constants.TOKEN)}")
    if (!viewModel.isLoggin) {
      val isAuthorize = viewModel.userLocalUseCase.isLoggin()
      viewModel.recallApi(isAuthorize)
    }else
      viewModel.getDetails(adsDetailsFragmentArgs.id, adsDetailsFragmentArgs.type)
    viewModel.adsAdapter.updateFavourite()
  }


  override fun onMapReady(p0: GoogleMap) {
    lifecycleScope.launch(Dispatchers.Main) {
//      delay(400)

      viewModel.mapConfig = MapConfig(requireContext(), p0)
      p0.uiSettings.isScrollGesturesEnabled = false;
      viewModel.mapConfig!!.setMapStyle() //set style google map
      viewModel.mapConfig!!.getGoogleMap()?.setMapStyle(context?.let {
        MapStyleOptions.loadRawResourceStyle(
          it, R.raw.map_style
        )
      })
      setLocation()
//      setMapStyle(context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map) })

    }
  }

  fun setLocation() {
    if (viewModel.mapConfig != null) {
      viewModel.store.get().let {
//        it.get().let {
        if (it?.latitude != 0.0) {
          Log.d(TAG, "setLocation: ${it?.latitude}")
          val location = it?.latitude?.let { it1 -> LatLng(it1, it.longitude) }
          location?.let { it1 ->
            MarkerOptions().position(it1)
              .title(it.name) // below line is use to add custom marker on our map.
              .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_location))
          }?.let { it2 ->
            viewModel.mapConfig!!.getGoogleMap()?.addMarker(
              it2
            )
          }
          viewModel.mapConfig!!.moveCamera(location)
        }
      }
    }
  }

  private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    // below line is use to generate a drawable.
    val vectorDrawable: Drawable? = ContextCompat.getDrawable(context, vectorResId)

    // below line is use to set bounds to our vector drawable.
    vectorDrawable?.setBounds(
      0,
      0,
      vectorDrawable.getIntrinsicWidth(),
      vectorDrawable.getIntrinsicHeight()
    )

    // below line is use to create a bitmap for our
    // drawable which we have added.
    vectorDrawable?.let {
      val bitmap: Bitmap = Bitmap.createBitmap(
        it.intrinsicWidth,
        it.intrinsicHeight,
        Bitmap.Config.ARGB_8888
      )
      val canvas = Canvas(bitmap)

      // below line is use to draw our
      // vector drawable in canvas.
      vectorDrawable.draw(canvas)
      return BitmapDescriptorFactory.fromBitmap(bitmap)

    }
    return null
    // below line is use to add bitmap in our canvas.

    // after generating our bitmap we are returning our bitmap.
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
//    val bundle = Bundle()
//    viewModel.store.get()?.id?.let { bundle.putInt(Constants.STORES_ID, it) }
//    viewModel.store.get()?.isFollowing?.let { bundle.putBoolean(Constants.FOLLOW, it) }
//    bundle.putBoolean(Constants.STORES_BLOCKED,viewModel.blockStore)
//    setFragmentResult(Constants.BUNDLE, bundle)
    super.onDestroy()
    viewModel.mapConfig?.getGoogleMap()?.clear()
  }

}