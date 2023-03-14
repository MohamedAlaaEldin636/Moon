package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.extensions.*
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

	val showRating = MutableLiveData(args.useRating)

	val comment = MutableLiveData("")

	fun addRate(view: View) {
		val fragment = view.findFragmentOrNull<RateInAdvDialogFragment>() ?: return

		val binding = fragment._binding ?: return

		val rate = if (args.useRating) binding.ratingBar.progress / 20 else null

		MyLogger.e("dasdasdasd ${binding.ratingBar.progress} $rate ${args.useRating}")

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				if (args.forAdNotStore) {
					repoShop.addReviewForAdv(args.advId, rate, comment.value.orEmpty())
				}else {
					repoShop.addReviewForStore(args.advId, rate, comment.value.orEmpty())
				}
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			fragment.findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
				true // Done successfully
			)
		}
	}

}
