package grand.app.moon.presentation.store.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.tabs.TabLayout
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentStoreDetailsBinding
import grand.app.moon.domain.utils.SpannedGridLayoutManager
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.ExploreFragmentDirections
import grand.app.moon.presentation.store.viewModels.StoreDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList

@AndroidEntryPoint
class StoreDetailsFragment : BaseFragment<FragmentStoreDetailsBinding>(), OnMapReadyCallback {

  private val adsDetailsFragmentArgs: StoreDetailsFragmentArgs by navArgs()

  private val viewModel: StoreDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_store_details

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.getDetails(adsDetailsFragmentArgs.id)
//    viewModel.exploreAdapter.clickEvent.observe(this,{
//      if(it != -1) {
//
//      }
//    })
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
            viewModel.update(
              resources.getString(R.string.google_direction_api),
              it.value.data,
              days
            )
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }

  fun scrollDown() {
    binding.scrollStoreDetails.scrollTo(0, binding.tabLayout.y.toInt())
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
    (childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment)
      ?.getMapAsync(this)
  }

  private val TAG = "AdsDetailsFragment"
  override fun onResume() {
    super.onResume()
    Log.d(TAG, "token: ${viewModel.userLocalUseCase.getKey(Constants.TOKEN)}")
    if (!viewModel.isLoggin) {
      val isAuthorize = viewModel.userLocalUseCase.isLoggin()
      viewModel.recallApi(isAuthorize)
    }
  }


  override fun onMapReady(p0: GoogleMap) {
    lifecycleScope.launch(Dispatchers.Main) {
      delay(500)
      viewModel.mapConfig = MapConfig(requireContext(), p0)
//      viewModel.mapConfig.setMapStyle() //set style google map
      viewModel.mapConfig.getGoogleMap()?.setMapStyle(context?.let {
        MapStyleOptions.loadRawResourceStyle(
          it,R.raw.map)
      })
//      setMapStyle(context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map) })
      val location = LatLng(viewModel.store.get()!!.latitude, viewModel.store.get()!!.longitude)
      viewModel.mapConfig.getGoogleMap()?.addMarker(
        MarkerOptions().position(location)
          .title(viewModel.store.get()!!.name) // below line is use to add custom marker on our map.
          .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_location))
      )

//      p0.addMarker(
//        MarkerOptions().title(viewModel.store.get()!!.name).position(
//          LatLng(
//            viewModel.store.get()!!.latitude,
//            viewModel.store.get()!!.longitude
//          )
//        )
//      )
      viewModel.mapConfig.getGoogleMap()?.moveCamera(CameraUpdateFactory.newLatLng(location))

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

//  private fun setRecyclerViewScrollListener() {
//    Log.d(TAG, "setRecyclerViewScrollListener: ")
//    val manager = SpannedGridLayoutManager(
//      object : SpannedGridLayoutManager.GridSpanLookup {
//        override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
//          var x = 0
//          if (position % 9 == 0) {
//            x = position / 9
//          }
//
//          return when {
//            position == 1 || x % 2 == 1 || (position - 1) % 18 == 0 ->
//              SpannedGridLayoutManager.SpanInfo(2, 2)
//            else ->
//              SpannedGridLayoutManager.SpanInfo(1, 1)
//          }
//
//        }
//      },
//      3,  // number of columns
//      1f // how big is default item
//    )
//    binding.rvStoreGallery.layoutManager = manager
//    binding.rvStoreGallery.adapter = viewModel.exploreAdapter
//
//  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.mapConfig.let {
      it.getGoogleMap()?.clear()
    }
  }

}