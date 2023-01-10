package grand.app.moon.presentation.myAds

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.shared.core.extensions.actOnGetIfNotInitialValueOrGetLiveData
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.compose.BaseTheme
import grand.app.moon.compose.ui.UILoading
import grand.app.moon.core.extenstions.showError
import grand.app.moon.databinding.FragmentAddAdvFinalPageBinding
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.addAdvFinalPage.CameraUtils
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel

@AndroidEntryPoint
class AddAdvFinalPageFragment : BaseFragment<FragmentAddAdvFinalPageBinding>(), PermissionsHandler.Listener {

	private val viewModel by viewModels<AddAdvFinalPageViewModel>()

	private var permissionsHandlerForProfileImage: PermissionsHandler? = null

	private val activityResultImageCameraFile = registerForActivityResult(
		ActivityResultContracts.TakePicture()
	) { result ->
		if (result != null && result) {
			CameraUtils.imageUri?.also { imageUri ->
				val newList = viewModel.listOfImages.value.orEmpty().toMutableList().also { mutableList ->
					if (viewModel.beforeCheckPermissionsIndexToShowImagesPopupMenu.value.orZero() < mutableList.size) {
						mutableList.removeAt(viewModel.beforeCheckPermissionsIndexToShowImagesPopupMenu.value.orZero())
					}

					mutableList.add(imageUri)
				}.toList()

				viewModel.setImages(newList)
			}
		}
	}

	private val activityResultImageGallery = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		MyLogger.e("on result -> it.resultCode == Activity.RESULT_OK ${it.resultCode} ${Activity.RESULT_OK}")
		if (it.resultCode == Activity.RESULT_OK) {
			var list = if (viewModel.beforeCheckPermissionsIndexToShowImagesPopupMenu.value == 0) {
				it.data?.clipData?.let { clipData ->
					List(clipData.itemCount) { index ->
						clipData.getItemAt(index).uri
					}.filterNotNull()
				}.orIfNullOrEmpty {
					listOfNotNull(it.data?.data)
				}
			}else {
				listOfNotNull(it.data?.data)
			}
			MyLogger.e("on result -> list $list ${viewModel.beforeCheckPermissionsIndexToShowImagesPopupMenu.value}")

			if (list.isEmpty()) {
				return@registerForActivityResult
			}

			var newList = viewModel.listOfImages.value.orEmpty().toMutableList().also { mutableList ->
				if (viewModel.beforeCheckPermissionsIndexToShowImagesPopupMenu.value.orZero() < mutableList.size) {
					mutableList.removeAt(viewModel.beforeCheckPermissionsIndexToShowImagesPopupMenu.value.orZero())
				}
			}.toList()

			if (newList.size + list.size > 22) {
				val amountToTake = 22 - newList.size

				showMessage(getString(R.string.only_var_have_been_picked, amountToTake.toString()))

				list = list.take(amountToTake)
			}

			newList = newList + list

			viewModel.setImages(newList)
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
						AddAdvFinalPageScreen(
							actOnAllPermissionsAcceptedOrRequestPermissions = {
								permissionsHandlerForProfileImage?.actOnAllPermissionsAcceptedOrRequestPermissions()
							},
							onCameraClick = ::pickImageFromCamera,
							onGalleryClick = ::pickImageFromGallery,
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
		viewModel.indexToShowImagesPopupMenu.value = viewModel
			.beforeCheckPermissionsIndexToShowImagesPopupMenu.value.orZero()
	}

	override fun onSubsetPermissionsAccepted(permissions: Map<String, Boolean>) {
		if (permissions[Manifest.permission.CAMERA] == true) {
			pickImageFromCamera()
		}else if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
			&& permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
			pickImageFromGallery()
		}
	}

	private fun pickImageFromCamera() {
		if (checkCountAndTagBeforePickingAnImageAndGetIfCanPickAnImage()) {
			activityResultImageCameraFile.launch(
				CameraUtils.createImageUri((this as? Fragment)?.activity ?: return)
			)
		}
	}

	private fun pickImageFromGallery() {
		if (checkCountAndTagBeforePickingAnImageAndGetIfCanPickAnImage()) {
			val intent = if (viewModel.indexToShowImagesPopupMenu.value == 0) {
				Intent(Intent.ACTION_GET_CONTENT).also {
					it.type = "image/*"
					it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
				}
			}else {
				Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
			}

			activityResultImageGallery.launch(intent)
		}
	}

	private fun checkCountAndTagBeforePickingAnImageAndGetIfCanPickAnImage(): Boolean {
		/*if (viewModel.listOfImages.value?.size.orZero() == 22) {
			context?.apply {
				showError(getString(R.string.max_num_of_images_is_22))
			}

			return false
		}

		it will always be replace not add
		*/

		return true
	}

}
