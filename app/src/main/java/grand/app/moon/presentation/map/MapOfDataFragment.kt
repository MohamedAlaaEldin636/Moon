package grand.app.moon.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.databinding.FragmentMapOfDataBinding
import grand.app.moon.databinding.ItemMapClusterBinding
import grand.app.moon.databinding.ItemMapImageAdvBinding
import grand.app.moon.databinding.ItemMapImageStoreBinding
import grand.app.moon.databinding.ItemMapImageStoryBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.map.model.MAClusterItem
import grand.app.moon.presentation.map.model.ResponseMapData
import grand.app.moon.presentation.map.viewModel.MapOfDataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.roundToInt

@AndroidEntryPoint
class MapOfDataFragment : BaseFragment<FragmentMapOfDataBinding>(), OnMapReadyCallback {

	companion object {
		fun goToThisScreenForStores(navController: NavController, categoryId: Int = -1, subCategoryId: Int = -1, propertyId: Int = -1) {
			goToThisScreen(navController, Type.STORE, categoryId, subCategoryId, propertyId)
		}
		fun goToThisScreenForAds(navController: NavController, categoryId: Int = -1, subCategoryId: Int = -1, propertyId: Int = -1) {
			goToThisScreen(navController, Type.ADVERTISEMENT, categoryId, subCategoryId, propertyId)
		}
		private fun goToThisScreen(
			navController: NavController,
			type: Type,
			categoryId: Int = -1,
			subCategoryId: Int = -1,
			propertyId: Int = -1
		) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.story.map.of.data",
				paths = arrayOf(type.toString(), categoryId.toString(), subCategoryId.toString(), propertyId.toString())
			)
		}
	}

	private val viewModel by viewModels<MapOfDataViewModel>()

	private val spacingBetweenDataItemsOnMapsAndScreenBorder by lazy {
		context?.dpToPx(48f)?.roundToInt().orZero()
	}

	private val clusterLayoutBackground by lazy {
		context?.createRectangleShape(5f, 48f, R.color.colorPrimary).orTransparent()
	}

	private val dataLayoutSize by lazy {
		context?.dpToPx(44f)?.roundToInt().orZero()
	}

	private lateinit var locationHandler: LocationHandler

	private val listenerForLocationHandler = object : LocationHandler.Listener {
		override fun showLoading() {
			this@MapOfDataFragment.showLoading()
		}

		override fun hideLoading() {
			this@MapOfDataFragment.hideLoading()
		}

		override fun onCurrentLocationResultSuccess(location: Location) {
			val latLng = LatLng(location.latitude, location.longitude)

			viewModel.myCurrentLocation = latLng
			// Draw your own location marker but then remove about current location thingy,
			// which is line having googleMap.isMyLocationEnabled = true

			val fragment = childFragmentManager.findFragmentById(R.id.mapFragmentContainerView)
				as? SupportMapFragment
			fragment?.getMapAsync(this@MapOfDataFragment)
		}

		override fun onCurrentLocationResultFailure(context: Context?, exception: Exception?) {
			showRetryErrorDialogWithBackNegativeButton(
				exception?.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_in_detecting_locations))
			) {
				locationHandler.requestCurrentLocation(false)
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		locationHandler = LocationHandler(
			this,
			lifecycle,
			requireContext(),
			listenerForLocationHandler
		)

		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoShop.getMapData(
					viewModel.args.type,
					viewModel.args.categoryId,
					viewModel.args.subCategoryId,
					viewModel.args.propertyId,
				)
			},
			showLoadingCode = {},
			hideLoadingCode = {}
		) { list ->
			val context = context ?: return@handleRetryAbleActionOrGoBack

			viewModel.allDataList = list

			viewLifecycleOwner.lifecycleScope.launch {
				viewModel.bitmapsDataMap.clear()
				viewModel.bitmapsDataMap = buildMap {
					for (item in viewModel.allDataList.orEmpty()) {
						this[item.id.orZero()] = when (viewModel.args.type) {
							Type.STORE -> {
								if (item.stories.isNullOrEmpty()) {
									val image = item.image.orEmpty()

									val bitmap = Glide.with(this@MapOfDataFragment)
										.asBitmap()
										.load(image)
										.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(dataLayoutSize, dataLayoutSize))
										.intoBitmap()

									val binding = ItemMapImageStoreBinding.bind(
										context.inflateLayout(R.layout.item_map_image_store)
									)

									bitmap?.also { binding.imageView.setImageBitmap(bitmap) }

									ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24).orTransparent().toBitmap()

									binding.root.toBitmap(dataLayoutSize).orDefaultPlaceBitmap(context)
								}else {
									val image = item.stories?.firstOrNull()?.file.orEmpty()

									val bitmap = Glide.with(this@MapOfDataFragment)
										.asBitmap()
										.load(image)
										.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(dataLayoutSize, dataLayoutSize))
										.intoBitmap()

									val binding = ItemMapImageStoryBinding.bind(
										context.inflateLayout(R.layout.item_map_image_story)
									)

									bitmap?.also { binding.imageView.setImageBitmap(bitmap) }

									ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24).orTransparent().toBitmap()

									binding.root.toBitmap(dataLayoutSize).orDefaultPlaceBitmap(context)
								}
							}
							Type.ADVERTISEMENT -> {
								context.createAdvItemBitmap(item)
							}
						}
					}
				}.toMutableMap()
			}
		}
	}

	private fun Context.createAdvItemBitmap(item: ResponseMapData): Bitmap {
		val binding = ItemMapImageAdvBinding.bind(
			inflateLayout(R.layout.item_map_image_adv)
		)

		val isSelected = viewModel.selectedMapData.value?.id == item.id

		binding.constraintLayout.background = ContextCompat.getDrawable(
			this,
			if (isSelected) R.drawable.dr_rect_5_border_2_star else R.drawable.dr_rect_5
		)

		binding.textView.text = "${item.price.orZero()} ${item.country?.currency.orEmpty()}"
		binding.textView.setTextColor(
			ContextCompat.getColor(this, if (isSelected) R.color.white else R.color.black)
		)

		return binding.root.toBitmapUsingIconGenerator(
			binding.constraintLayout.background
		).orDefaultClusterBitmap(this)
	}

	private fun Bitmap?.orDefaultPlaceBitmap(context: Context): Bitmap {
		return this ?: ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24)
			.orTransparent().toBitmap()
	}
	private fun Bitmap?.orDefaultClusterBitmap(context: Context): Bitmap {
		return this ?: ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_circle_24)
			.orTransparent().toBitmap()
	}

	override fun getLayoutId(): Int = R.layout.fragment_map_of_data

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return super.onCreateView(inflater, container, savedInstanceState).also {
			showLoading()

			locationHandler.requestCurrentLocation(false)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerViewCategories.setupWithRVItemCommonListUsage(
			viewModel.adapterCategories,
			true,
			1
		)

		viewModel.selectedCategory.distinctUntilChanged().ignoreFirstTimeChanged().observe(viewLifecycleOwner) {
			updateDataToMapThenAnimateCamera()
		}
	}

	override fun onMapReady(googleMap: GoogleMap) {
		val context = context ?: return

		viewModel.googleMap = googleMap

		googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE//MAP_TYPE_TERRAIN
		//googleMap.isMyLocationEnabled = true // if will be removed check onCurrentLocationResultSuccess fun.
		googleMap.addMarker(
			MarkerOptions().position(viewModel.myCurrentLocation.orZero())
				.icon(BitmapDescriptorFactory.fromBitmap(
					ContextCompat.getDrawable(
						context, R.drawable.my_current_location_pin
					).orTransparent().toBitmap()
				))
		)?.tag = R.id.marker_tag

		val clusterManager = ClusterManager<MAClusterItem>(context, googleMap)
		viewModel.clusterManager = clusterManager
		viewModel.clusterManager?.renderer = object : DefaultClusterRenderer<MAClusterItem>(context, googleMap, clusterManager) {
			override fun onBeforeClusterItemRendered(item: MAClusterItem, markerOptions: MarkerOptions) {
				markerOptions.drawDataItem(item)
			}
			override fun onClusterItemUpdated(item: MAClusterItem, marker: Marker) {
				marker.drawDataItem(item)
			}
			override fun onClusterItemRendered(item: MAClusterItem, marker: Marker) {
				marker.drawDataItem(item)
			}

			override fun onBeforeClusterRendered(
				cluster: Cluster<MAClusterItem>,
				markerOptions: MarkerOptions
			) {
				markerOptions.drawCluster(cluster)
			}
			override fun onClusterUpdated(cluster: Cluster<MAClusterItem>, marker: Marker) {
				marker.drawCluster(cluster)
			}
			override fun onClusterRendered(cluster: Cluster<MAClusterItem>, marker: Marker) {
				marker.drawCluster(cluster)
			}
		}.also {
			//it.minClusterSize = ; //todo in px same as the drawing isa. + in case of click on cluster and reached max zoom show list of stores isa.
		}
		viewModel.clusterManager?.setOnClusterItemClickListener { maClusterItem ->
			viewModel.allDataList?.firstOrNull { it.id == maClusterItem?.id }?.also { mapData ->
				when (viewModel.args.type) {
					Type.STORE -> {
						this.context?.also { context ->
							viewModel.userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
								context,
								findNavController(),
								mapData
							)
						}
					}
					Type.ADVERTISEMENT -> {
						val oldItem = viewModel.selectedMapData.value
						val newItem = viewModel.allDataList?.firstOrNull { it.id == maClusterItem?.id }

						if (newItem == null || oldItem?.id == newItem.id) return@also

						viewModel.selectedMapData.value = viewModel.allDataList?.firstOrNull {
							it.id == newItem.id
						}

						//val list = viewModel.clusterManager?.algorithm?.items?.filterNotNull().orEmpty()
						if (oldItem?.id != null) {
							viewModel.bitmapsDataMap[oldItem.id.orZero()] = context.createAdvItemBitmap(oldItem)
						}

						viewModel.bitmapsDataMap[newItem.id.orZero()] = context.createAdvItemBitmap(newItem)

						viewModel.clusterManager?.cluster()

						maClusterItem?.also {
							viewModel.googleMap?.animateCamera(
								CameraUpdateFactory.newLatLng(it.position)
							)
						}
					}
				}
			}

			/*
			true if the listener consumed the event (i.e. the default behavior should not occur),
			false otherwise (i.e. the default behavior should occur).
			The default behavior is for the camera to move to the marker and an info window to appear.
			 */
			true
		}
		viewModel.clusterManager?.setOnClusterClickListener { cluster ->
			if (cluster != null) {
				viewModel.googleMap?.animateCamera(
					CameraUpdateFactory.newLatLngZoom(
						cluster.position,
						viewModel.googleMap?.cameraPosition?.zoom.orZero().inc().inc()
					)
				)
			}

			/*
			Called when cluster is clicked.
			Return true if click has been handled
			Return false and the click will dispatched to the next listener
			 */
			true
		}

		googleMap.setOnCameraIdleListener(viewModel.clusterManager)
		googleMap.setOnMarkerClickListener(viewModel.clusterManager)

		addFirstTimeDataToMapThenAnimateCamera()
	}

	private fun addFirstTimeDataToMapThenAnimateCamera() {
		viewLifecycleOwner.lifecycleScope.launch {
			while (
				viewModel.clusterManager == null
				|| viewModel.allDataList == null
				|| viewModel.bitmapsDataMap.size != viewModel.allDataList?.size
			) {
				delay(50)
			}

			viewModel.clusterManager?.setAnimation(true)

			updateDataToMapThenAnimateCamera()
		}
	}

	private fun updateDataToMapThenAnimateCamera() {
		showLoading()

		val list = viewModel.getFilteredDataList()

		viewModel.clusterManager?.clearItems()
		if (list.isNotEmpty()) {
			viewModel.clusterManager?.addItems(
				list.map {
					MAClusterItem(
						it.id.orZero(),
						it.latitude.orZero(),
						it.longitude.orZero()
					)
				}
			)

			viewModel.googleMap?.animateCamera(
				CameraUpdateFactory.newLatLngBounds(
					list.map {
						LatLng(it.latitude.orZero(), it.longitude.orZero())
					}.toLatLngBounds(),
					spacingBetweenDataItemsOnMapsAndScreenBorder
				)
			)
		}else {
			val msg = when (viewModel.args.type) {
				Type.STORE -> getString(R.string.no_stores_found)
				Type.ADVERTISEMENT -> getString(R.string.no_ads_found)
			}

			showMessage(msg)
		}

		// Must be called after adding, updating or removing items isa.
		viewModel.clusterManager?.cluster()

		hideLoading()
	}

	private fun MarkerOptions.drawDataItem(item: MAClusterItem) {
		val context = context ?: return

		val bitmap = viewModel.bitmapsDataMap[item.id].orDefaultPlaceBitmap(context)

		icon(BitmapDescriptorFactory.fromBitmap(bitmap))
	}
	private fun Marker.drawDataItem(item: MAClusterItem) {
		val context = context ?: return

		val bitmap = viewModel.bitmapsDataMap[item.id].orDefaultPlaceBitmap(context)

		setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
	}

	private fun MarkerOptions.drawCluster(cluster: Cluster<MAClusterItem>) {
		val context = context ?: return

		icon(BitmapDescriptorFactory.fromBitmap(context.getClusterBitmap(cluster.size)))
	}
	private fun Marker.drawCluster(cluster: Cluster<MAClusterItem>) {
		val context = context ?: return

		setIcon(BitmapDescriptorFactory.fromBitmap(context.getClusterBitmap(cluster.size)))
	}

	private fun Context.getClusterBitmap(size: Int): Bitmap {
		return (viewModel.bitmapsClustersMap[size] ?: ItemMapClusterBinding.bind(
			inflateLayout(R.layout.item_map_cluster)
		).let{ binding ->
			val maxVisibleCount = 99

			binding.textView.text = if (size > maxVisibleCount) "+$maxVisibleCount" else "$size"

			binding.root.toBitmapUsingIconGenerator(clusterLayoutBackground)
		}).orDefaultClusterBitmap(this)
	}

	override fun onDestroyView() {
		viewModel.clusterManager?.clearItems()
		viewModel.clusterManager?.cluster()

		viewModel.googleMap = null

		viewModel.clusterManager = null

		super.onDestroyView()
	}

	enum class Type(val apiValue: String) {
		STORE("store"),
		ADVERTISEMENT("advertisement")
	}

}
