package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemReviewInAdvDetailsBinding
import grand.app.moon.databinding.ItemReviewInClientsReviewsInAdvDetailsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.RVItemCommonListUsage
import grand.app.moon.extensions.RVPagingItemCommonListUsage
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrElseResNameBA
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.setProgressBA
import grand.app.moon.presentation.myAds.AdvClientsReviewsFragmentArgs
import grand.app.moon.presentation.myAds.model.ItemReviewInAdvDetails
import javax.inject.Inject

@HiltViewModel
class AdvClientsReviewsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: AdvClientsReviewsFragmentArgs,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val user by lazy {
		userLocalUseCase()
	}

	val userId by lazy {
		user.id
	}

	val reviews = repoShop.getClientsReviewsForAdv(args.advId)

	val adapter = RVPagingItemCommonListUsage<ItemReviewInClientsReviewsInAdvDetailsBinding, ItemReviewInAdvDetails>(
		R.layout.item_review_in_clients_reviews_in_adv_details,
	) { binding, _, item ->
		binding.nameTextView.text = item.user?.name.orEmpty()

		binding.commentTextView.text = item.review.orEmpty()

		binding.dateTextView.text = item.date.orEmpty()

		binding.ratingBar.isVisible = item.user?.id != userId
		binding.ratingBar.setProgressBA(item.rate.orZero() * 20)

		binding.imageView.setupWithGlideOrElseResNameBA(item.user?.image, "ic_default_user")
	}

	fun addReview(view: View) {
		//TODO()
	}

}
