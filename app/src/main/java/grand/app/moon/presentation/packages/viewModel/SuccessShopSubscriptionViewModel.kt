package grand.app.moon.presentation.packages.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.extensions.app
import grand.app.moon.presentation.packages.SuccessShopSubscriptionFragmentArgs
import javax.inject.Inject

@HiltViewModel
class SuccessShopSubscriptionViewModel @Inject constructor(
	application: Application,
	val args: SuccessShopSubscriptionFragmentArgs,
) : AndroidViewModel(application) {

	val buttonText = MutableLiveData(
		if (args.willGoToCreateShopNotMyPackage) {
			app.getString(R.string.create_your_store_now)
		}else {
			app.getString(R.string.check_ad_premium)
		}
	)

	fun goToNextPage(view: View) {
		view.findNavController().navigateUp()
	}

}
