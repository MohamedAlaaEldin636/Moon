package grand.app.moon.presentation.myAds

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.shared.core.extensions.actOnGetIfNotInitialValueOrGetLiveData
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.compose.BaseTheme
import grand.app.moon.compose.ui.UILoading
import grand.app.moon.databinding.FragmentAddAdvFinalPageBinding
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.*
import grand.app.moon.helpers.utils.getUriFromBitmapRetrievedByCamera
import grand.app.moon.helpers.utils.handleCaptureImageRotation
import grand.app.moon.presentation.auth.profile.ProfileFragmentDirections
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.navigateSafe
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.addAdvFinalPage.CameraUtils
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAdvFinalPageFragment : BaseFragment<FragmentAddAdvFinalPageBinding>(), PermissionsHandler.Listener {

	private val viewModel by viewModels<AddAdvFinalPageViewModel>()

	var permissionsHandlerForProfileImage: PermissionsHandler? = null
		private set

	private val activityResultImageCameraFile = registerForActivityResult(
		ActivityResultContracts.TakePicture()
	) {
		if (it != null && it && CameraUtils.imageUri != null) {
			viewModel.addImage(CameraUtils.tag ?: return@registerForActivityResult, CameraUtils.imageUri ?: return@registerForActivityResult)
			/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				val bitmap = handleCaptureImageRotation(fileCameraCapture,imageUri)
				bitmap?.let {
					imageUri = getUriFromBitmapRetrievedByCamera(it)
					viewModel.request.uri = imageUri
					loadImageProfile()
					navigateSafe(ProfileFragmentDirections.actionProfileFragmentToCropFragment(viewModel.request))
				}
			}*/
		}
	}

	private val activityResultImageGallery = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		if (it.resultCode == Activity.RESULT_OK) {
			/*Hassan Project ... val list = it.data?.clipData?.let { clipData ->
				List(clipData.itemCount) { index ->
					clipData.getItemAt(index).uri
				}.filterNotNull()
			}.orIfNullOrEmpty {
				listOfNotNull(it.data?.data)
			}*/
			val uri = it.data?.data ?: return@registerForActivityResult

			viewModel.addImage(CameraUtils.tag ?: return@registerForActivityResult, uri)

			/*if (*//*list.size > 12 || *//*viewModel.adapter.currentList.size + list.size > 12) {
				context?.showNormalToast(getString(SR.string.only_12_have_been_picked))
			}

			val amountToTake = 12 - viewModel.adapter.currentList.size

			if (amountToTake > 0) {
				viewModel.adapter.addItemsUri(list.take(amountToTake))
			}*/
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_add_adv_final_page

	override fun onCreate(savedInstanceState: Bundle?) {
		permissionsHandlerForProfileImage = PermissionsHandler(
			this,
			lifecycle,
			requireContext(),
			listOf(
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.CAMERA,
			),
			this
		)

		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.adsUseCase.getFilterDetails2Suspend(viewModel.args.idOfMainCategory, viewModel.args.idOfSubCategory)
			}
		) {
			viewModel.properties.value = it.properties
			val map = mutableMapOf<Int, ItemProperty>()
			for (property in it.properties.orEmpty()) {
				map[property.id.orZero()] = property
			}
			viewModel.mapOfProperties.value = map
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
			setContent {
				BaseTheme {
					UILoading.TmpScreen(showLoading = false) {
						val context = LocalContext.current

						AddAdvFinalPageScreen(
							actOnAllPermissionsAcceptedOrRequestPermissions = {
								permissionsHandlerForProfileImage?.actOnAllPermissionsAcceptedOrRequestPermissions()
							},
							onCameraClick = {
								activityResultImageCameraFile.launch(CameraUtils.createImageUri(context))
							},
							onGalleryClick = {
								activityResultImageGallery.launch(
									Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
								)
							},
							addAdvertisement = {
								viewModel.addAdvertisement(this@AddAdvFinalPageFragment)
							},
							goGetAddress = { viewModel.goToMapToGetAddress(this@AddAdvFinalPageFragment) },
							showOrGetCities = {
								if (viewModel.cities.isEmpty()) {
									handleRetryAbleActionCancellable(
										action = { viewModel.countriesUseCase.getCities() }
									) { list ->
										viewModel.cities = list

										if (list.isEmpty()) {
											return@handleRetryAbleActionCancellable showMessage(getString(R.string.no_cities_found))
										}

										viewModel.showCitiesPopupMenu.value = true
									}
								}else {
									viewModel.showCitiesPopupMenu.value = true
								}
							}
						)
					}
				}
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
			LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
			"",
			viewLifecycleOwner,
			{ !it.isNullOrEmpty() }
		) {
			viewModel.locationData.value = it.fromJsonInlinedOrNull<LocationData>()
		}
	}

	override fun onAllPermissionsAccepted() {
		viewModel.showImagesPopupMenu.value = true
	}

	override fun onSubsetPermissionsAccepted(permissions: Map<String, Boolean>) {
		if (permissions[Manifest.permission.CAMERA] == true) {
			activityResultImageCameraFile.launch(
				CameraUtils.createImageUri((this as Fragment).activity ?: return)
			)
		}else if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
			&& permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
			activityResultImageGallery.launch(
				Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
			)
		}
	}

	/*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		if (viewModel.cities.isEmpty()) {
			handleRetryAbleActionCancellable(
				action = { viewModel.countriesUseCase.getCities() }
			) { list ->
				viewModel.cities = list

				viewModel.showCitiesPopupMenu.value = true
			}
		}else {
			viewModel.showCitiesPopupMenu.value = true
		}
	}*/

	/*override fun setBindingVariables() {
		binding.viewModel = viewModel
	}*/

	/*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}*/

}
