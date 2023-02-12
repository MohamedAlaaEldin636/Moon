package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemReviewInClientsReviewsInAdvDetailsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.shop.ResponseClientReviews
import grand.app.moon.domain.shop.ResponseReviewsWithStats
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrElseResNameBA
import grand.app.moon.presentation.myAds.AdvClientsReviewsFragment
import grand.app.moon.presentation.myAds.AdvClientsReviewsFragmentArgs
import javax.inject.Inject

@HiltViewModel
class AdvClientsReviewsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: AdvClientsReviewsFragmentArgs,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val response = MutableLiveData<ResponseReviewsWithStats?>()

	val textAverageRate = response.map { "${app.getString(R.string.average_rate)} ( ${it?.averageRate.orZero()} )" }
	val textRateCount = response.map { "${app.getString(R.string.rate_count)} ${it?.rateCount.orZero()}" }

	val textStar5 = response.map { "${it?.countStar5.orZero()} ${app.getString(R.string.user)}" }
	val textStar4 = response.map { "${it?.countStar4.orZero()} ${app.getString(R.string.user)}" }
	val textStar3 = response.map { "${it?.countStar3.orZero()} ${app.getString(R.string.user)}" }
	val textStar2 = response.map { "${it?.countStar2.orZero()} ${app.getString(R.string.user)}" }
	val textStar1 = response.map { "${it?.countStar1.orZero()} ${app.getString(R.string.user)}" }

	val user by lazy {
		userLocalUseCase()
	}

	val userId by lazy {
		user.id
	}

	val reviews = repoShop.getReviewsForAdvPaging(args.advId)

	val adapter = RVPagingItemCommonListUsage<ItemReviewInClientsReviewsInAdvDetailsBinding, ResponseClientReviews>(
		R.layout.item_review_in_clients_reviews_in_adv_details,
	) { binding, _, item ->
		binding.nameTextView.text = item.user?.name.orEmpty()

		binding.commentTextView.text = item.review.orEmpty()

		binding.dateTextView.text = item.date.orEmpty()

		binding.ratingBar.isVisible = item.user?.id != userId
		binding.ratingBar.setProgressBAFloat(item.rate.orZero() * 20f)

		binding.imageView.setupWithGlideOrElseResNameBA(item.user?.image, "ic_default_user")
	}

	fun addReview(view: View) {
		val fragment = view.findFragmentOrNull<AdvClientsReviewsFragment>() ?: return

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.rate.in.adv.dialog",
			paths = arrayOf(args.advId.toString(), args.useRating.toString())
		)
	}

}
