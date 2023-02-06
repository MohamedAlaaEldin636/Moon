package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.getString
import grand.app.moon.extensions.navigateSafely
import grand.app.moon.presentation.myAds.AddAdvertisementFragment
import grand.app.moon.presentation.myAds.AddAdvertisementFragmentArgs
import grand.app.moon.presentation.myAds.AddAdvertisementFragmentDirections
import javax.inject.Inject

@HiltViewModel
class AddAdvertisementViewModel @Inject constructor(
	application: Application,
	args: AddAdvertisementFragmentArgs,
	val homeUseCase: HomeUseCase
) : AndroidViewModel(application) {

	val currentlySelectedTypeIsFree = MutableLiveData(args.availableNumOfAdvertisements > 0)

	val enableFreeAdType = args.availableNumOfAdvertisements > 0
	
	val freeAdvBackgroundRes = currentlySelectedTypeIsFree.map {
		if (it) R.drawable.dr_round_selected else R.drawable.dr_round_not_selected
	}
	val freeAdvTintColor = currentlySelectedTypeIsFree.map {
		if (it) R.color.white else R.color.not_selected_3
	}

	val paidAdvBackgroundRes = currentlySelectedTypeIsFree.map {
		if (it) R.drawable.dr_round_not_selected else R.drawable.dr_round_selected
	}
	val paidAdvTintColor = currentlySelectedTypeIsFree.map {
		if (it) R.color.not_selected_3 else R.color.white
	}

	val availableAdvertisementsText = currentlySelectedTypeIsFree.map {
		if (it) {
				"${getString(R.string.ad_benefit_1)} ${args.availableNumOfAdvertisements}"
			} else {
				getString(R.string.ad_benefit_1_paid)
			}
	}

	val buttonText = currentlySelectedTypeIsFree.map {
		if (it) getString(R.string.add_free_adv) else getString(R.string.pick_your_package_now)
	}

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

	fun goToNextScreen(view: View) {
		val fragment = view.findFragmentOrNull<AddAdvertisementFragment>() ?: return

		goToNextScreen(fragment)
	}
	fun goToNextScreen(fragment: AddAdvertisementFragment) {
		if (currentlySelectedTypeIsFree.value == true) {
			fragment.findNavController().navigateSafely(
				AddAdvertisementFragmentDirections.actionDestAddAdvertisementToDestAddAdvCategoriesList()
			)
		}else {
			fragment.findNavController().navigateSafely(
				AddAdvertisementFragmentDirections.actionDestAddAdvertisementToDestBecomeShopPackages()
			)
		}
	}

	fun toggleAdType(selectFree: Boolean) {
		currentlySelectedTypeIsFree.value = selectFree
	}

}
