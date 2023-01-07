package grand.app.moon.presentation.myAds

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
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
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAdvFinalPageFragment : BaseFragment<FragmentAddAdvFinalPageBinding>(), PermissionsHandler.Listener {

	private val viewModel by viewModels<AddAdvFinalPageViewModel>()

	private var permissionsHandlerForProfileImage: PermissionsHandler? = null

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
		TODO("Not yet implemented")
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
