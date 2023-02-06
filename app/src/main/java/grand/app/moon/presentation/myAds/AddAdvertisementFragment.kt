package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAddAdvertisementBinding
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.navigateSafely
import grand.app.moon.extensions.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.AddAdvertisementViewModel

@AndroidEntryPoint
class AddAdvertisementFragment : BaseFragment<FragmentAddAdvertisementBinding>()  {

	private val viewModel by viewModels<AddAdvertisementViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_add_advertisement

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			if (it) {
				handleRetryAbleActionOrGoBack(
					action = {
						viewModel.homeUseCase.checkAvailableAdvertisements()
					}
				) { availableCount ->
					val navController = findNavController()

					navController.navigateUp()

					if (availableCount > 0) {
						navController.navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest.add.adv.categories.list"
						)
					}else {
						navController.navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest.my.become.shop.package"
						)
					}
				}
			}
		}
	}

}
