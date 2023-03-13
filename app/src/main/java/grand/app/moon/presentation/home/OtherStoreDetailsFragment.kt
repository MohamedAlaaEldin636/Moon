package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.databinding.FragmentOtherStoreDetailsBinding
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.utils.MACometChatUtils
import grand.app.moon.presentation.home.viewModels.OtherStoreDetailsViewModel
import grand.app.moon.presentation.myAds.MyAdsFragment
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class OtherStoreDetailsFragment : BaseFragment<FragmentOtherStoreDetailsBinding>(),
	OnMapReadyCallback {

	companion object {
		const val KEY_RESULT_IS_FOLLOWING = "OtherStoreDetailsFragment.KEY_RESULT_IS_FOLLOWING"

		fun launch(navController: NavController, id: Int, fromViewNotSearch: Boolean = true) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.other.store.details",
				paths = arrayOf(id.toString(), fromViewNotSearch.toString())
			)
		}
	}

	private val viewModel by viewModels<OtherStoreDetailsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_other_store_details

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				if (viewModel.args.fromViewNotSearch) {
					viewModel.repoShop.getStoreDetailsFromView(viewModel.args.id)
				}else {
					viewModel.repoShop.getStoreDetailsFromSearch(viewModel.args.id)
				}
			},
			hideLoadingCode = {}
		) { response ->
			viewModel.response.value = response.copy(viewsCount = response.viewsCount?.inc())

			viewModel.adapterHighlights.submitList(
				response.highlights.orEmpty()
			)
			viewModel.showHighlights.value = response.highlights.isNullOrEmpty().not()

			val listOfSocialMedia = buildList {
				addAll(response.socialMediaLinks.orEmpty().filter { it.linkUrl.isNullOrEmpty().not() })

				if (response.advertisingWebsite.isNullOrEmpty().not()) {
					this += ResponseStoreSocialMedia(
						ResponseStoreSocialMedia.STORE_DETAILS_ADVERTISING_WEBSITE_API_TYPE,
						response.advertisingWebsite
					)
				}

				if (response.shareLink.isNullOrEmpty().not()) {
					this += ResponseStoreSocialMedia(
						ResponseStoreSocialMedia.STORE_DETAILS_COPY_lINK_API_TYPE,
						response.shareLink
					)
				}
			}
			viewModel.adapterSocialMedia.submitList(listOfSocialMedia)
			viewModel.showSocialMedia.value = listOfSocialMedia.isNotEmpty()

			viewModel.adapterExplores.submitList(response.explores.orEmpty())

			val storeCategories = listOf(ItemCategory(null, getString(R.string.all))).plus(
				response.storeCategories.orEmpty()
			)
			viewModel.allCategories.value = storeCategories
			viewModel.adapterCategories.submitList(storeCategories)
			val storeSubCategories = response.storeCategories.orEmpty().flatMap {
				it.subCategories.orEmpty()
			}
			viewModel.allSubCategories.value = storeSubCategories
			viewModel.adapterSubCategories.submitList(
				listOf(ItemSubCategory(name = getString(R.string.all))) + storeSubCategories
			)

			viewModel.adapterAds.submitList(response.advertisements.orEmpty())

			lifecycleScope.launch {
				viewModel.showIsOnline.value = MACometChatUtils.isUserOnline(response.id.orZero())

				hideLoading()

				if (MyApplication.deepLinkUri?.getQueryParameter("story").isNullOrEmpty().not()) {
					//https://om.sooqmoon.net/website/ar/shop/7779/mariz-store?story=view
					if (response.stories.isNullOrEmpty()) {
						showMessage(getString(R.string.no_stories_found))
					}else {
						viewModel.showStories(binding.cardOwnerImageView)
					}
				}

				MyApplication.deepLinkUri = null
			}
		}
	}

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return super.onCreateView(inflater, container, savedInstanceState).also {
			val fragment = childFragmentManager.findFragmentById(R.id.mapFragmentContainerView)
				as? SupportMapFragment
			fragment?.getMapAsync(this)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerViewStories.setupWithRVItemCommonListUsage(
			viewModel.adapterHighlights,
			true,
			1
		)

		binding.recyclerViewSocialMedia.setupWithRVItemCommonListUsage(
			viewModel.adapterSocialMedia,
			true,
			1
		)

		binding.recyclerViewExplores.layoutManager = requireContext().getExploreLayoutManager()
		binding.recyclerViewExplores.adapter = viewModel.adapterExplores

		binding.recyclerViewAds.setupWithRVItemCommonListUsage(
			viewModel.adapterAds,
			false,
			2,
			onGridLayoutSpanSizeLookup = {
				if (viewModel.layoutIsTwoColNotOneCol.value == true) 1 else 2
			}
		)

		binding.recyclerViewStoreCategories.setupWithRVItemCommonListUsage(
			viewModel.adapterCategories,
			true,
			1
		)

		binding.recyclerViewStoreSubCategories.setupWithRVItemCommonListUsage(
			viewModel.adapterSubCategories,
			true,
			1
		)

		viewModel.layoutIsTwoColNotOneCol.distinctUntilChanged().ignoreFirstTimeChanged().observe(viewLifecycleOwner) {
			binding.recyclerViewAds.layoutManager?.requestLayout()
		}

		viewModel.toBeShownSubCategories.observe(viewLifecycleOwner) {
			viewModel.adapterSubCategories.submitList(it.orEmpty())
		}

		viewModel.toBeShownAds.observe(viewLifecycleOwner) {
			viewModel.adapterAds.submitList(it)
		}

		binding.root.post {
			val height = (binding.root.height.toFloat() * 0.5f).roundToInt()

			val layoutParams = binding.recyclerViewExplores.layoutParams
			layoutParams.height = height
			binding.recyclerViewExplores.layoutParams = layoutParams

			val layoutParams1 = binding.recyclerViewAds.layoutParams
			layoutParams1.height = height
			binding.recyclerViewAds.layoutParams = layoutParams1
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<MyAdsFragment.NewAdvertisementState> {
			if (it == MyAdsFragment.NewAdvertisementState.BECAME_PREMIUM) {
				findNavController().currentBackStackEntry?.savedStateHandle?.remove<MyAdsFragment.NewAdvertisementState>(
					AppConsts.NavController.GSON_KEY
				)

				handleRetryAbleActionOrGoBack(
					action = {
						if (viewModel.args.fromViewNotSearch) {
							viewModel.repoShop.getStoreDetailsFromView(viewModel.args.id)
						}else {
							viewModel.repoShop.getStoreDetailsFromSearch(viewModel.args.id)
						}
					},
				) { response ->
					viewModel.response.value = viewModel.response.value?.copy(
						advertisements = response.advertisements
					)
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()

		makeStatusBarTransparentWithWhiteIcons()
	}

	override fun onPause() {
		makeStatusBarToPrimaryDarkWithWhiteIcons()

		super.onPause()
	}

	override fun onMapReady(googleMap: GoogleMap) {
		lifecycleScope.launch {
			suspendUntilNotNull {
				viewModel.response.value
			}

			val location = LatLng(
				viewModel.response.value?.latitude?.toDoubleOrNull() ?: return@launch,
				viewModel.response.value?.longitude?.toDoubleOrNull() ?: return@launch
			)

			googleMap.addMarker(
				MarkerOptions()
					.position(location)
					.iconDrawableRes(context, R.drawable.icon_location_on_map_4)
			)

			googleMap.animateCamera(
				CameraUpdateFactory.newLatLngZoom(
					location,
					15f
				)
			)
		}
	}

}