package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.handleRetryAbleActionCancellableNullable
import grand.app.moon.extensions.navUpThenSetResultInBackStackEntrySavedStateHandleViaGson
import grand.app.moon.extensions.setProgressBA
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.RateInAdvDialogFragment
import grand.app.moon.presentation.myAds.RateInAdvDialogFragmentArgs
import javax.inject.Inject

@HiltViewModel
class RateInAdvViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: RateInAdvDialogFragmentArgs,
) : AndroidViewModel(application) {

	val showRating = MutableLiveData(true/*args.useRating*/)

	val comment = MutableLiveData("")

	fun addRate(view: View) {
		val fragment = view.findFragmentOrNull<RateInAdvDialogFragment>() ?: return

		val binding = fragment._binding ?: return

		val rate = if (true/*args.useRating*/) binding.ratingBar.progress / 20 else null

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.addReviewForAdv(args.advId, rate, comment.value.orEmpty())
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			fragment.findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
				true // Done successfully
			)
		}
	}

}
