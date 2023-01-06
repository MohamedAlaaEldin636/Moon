package grand.app.moon.presentation.myAds

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
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.compose.BaseTheme
import grand.app.moon.compose.ui.UILoading
import grand.app.moon.databinding.FragmentAddAdvFinalPageBinding
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.handleRetryAbleActionCancellable
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAdvFinalPageFragment : BaseFragment<FragmentAddAdvFinalPageBinding>()  {

	private val viewModel by viewModels<AddAdvFinalPageViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_add_adv_final_page

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
