package grand.app.moon.presentation.store.views

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
import grand.app.moon.presentation.store.viewModels.StoreDetailsViewModel
import kotlinx.coroutines.flow.collect
import javax.security.auth.callback.Callback

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

    viewModel.clickEvent.observe(viewLifecycleOwner, {
      if (it == Constants.LOGIN_REQUIRED) openLoginActivity()
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

  override fun setUpViews() {
    super.setUpViews()
    binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

      override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab?.position){
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

  override fun onStart() {
    super.onStart()
//    Toast.makeText(context, "onStart", Toast.LENGTH_SHORT).show()
  }

  override fun onMapReady(p0: GoogleMap) {
    viewModel.mapConfig = MapConfig(requireContext(), p0)
    viewModel.mapConfig.setMapStyle() //set style google map
    p0.addMarker(MarkerOptions().title(viewModel.store.get()!!.name).position(LatLng(
      viewModel.store.get()!!.latitude,
      viewModel.store.get()!!.longitude
    )))
  }

  private fun setRecyclerViewScrollListener() {

    val manager = SpannedGridLayoutManager(
      object : SpannedGridLayoutManager.GridSpanLookup {
        override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
          var x = 0
          if (position % 9 == 0) {
            x = position / 9
          }

          return when {
            position == 1 || x % 2 == 1 || (position - 1) % 18 == 0 ->
              SpannedGridLayoutManager.SpanInfo(2, 2)
            else ->
              SpannedGridLayoutManager.SpanInfo(1, 1)
          }

        }
      },
      3,  // number of columns
      1f // how big is default item
    )
    binding.rvStoreGallery.layoutManager = manager
    binding.rvStoreGallery.adapter = viewModel.exploreAdapter

  }

}