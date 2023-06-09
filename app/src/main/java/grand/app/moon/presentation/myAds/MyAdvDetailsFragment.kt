package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMyAdvDetailsBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.myAds.model.ItemReviewInAdvDetails
import grand.app.moon.presentation.myAds.model.ItemUserInReviewsInAdvDetails
import grand.app.moon.presentation.myAds.viewModel.MyAdvDetailsViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyAdvDetailsFragment : BaseFragment<FragmentMyAdvDetailsBinding>() {

	private val viewModel by viewModels<MyAdvDetailsViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (viewModel.args.jsonOfResponseMyAdvDetails.isNullOrEmpty()) {
			handleRetryAbleActionCancellable(
				action = {
					viewModel.adsUseCase.getMyAdvertisementDetails(viewModel.args.id)
				}
			) {
				viewModel.response.value = it
			}
		}
	}

	override fun onResume() {
		super.onResume()

		makeStatusBarTransparentWithWhiteIcons()
	}

	override fun onPause() {
		makeStatusBarToPrimaryDarkWithWhiteIcons()

		super.onPause()
	}

	override fun getLayoutId(): Int = R.layout.fragment_my_adv_details

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.sliderView.setSliderAdapter(viewModel.adapterImages)

		binding.recyclerViewStats.setupWithRVItemCommonListUsage(
			viewModel.adapterStats,
			false,
			2
		)

		binding.recyclerViewProperties.setupWithRVItemCommonListUsage(
			viewModel.adapterProperties,
			false,
			1
		)

		binding.recyclerViewSwitches.setupWithRVItemCommonListUsage(
			viewModel.adapterSwitches,
			false,
			3
		)

		binding.recyclerViewReviews.setupWithRVItemCommonListUsage(
			viewModel.adapterReviews,
			false,
			1
		)

		viewModel.response.observe(viewLifecycleOwner) { response ->
			if (response == null) return@observe

			viewModel.adapterImages.submitList(
				response.images?.map { it.image.orEmpty() }.orEmpty()
			)
			binding.sliderView.post {
				binding.sliderView.setSliderAdapter(viewModel.adapterImages)
				binding.sliderView.startAutoCycle()
			}

			viewModel.adapterStats.submitList(
				response.statistics.orEmpty()
			)

			viewModel.adapterProperties.submitList(
				response.properties.orEmpty()
			)

			viewModel.adapterSwitches.submitList(
				response.switches.orEmpty()
			)

			val reviews = if (response.reviews.isNullOrEmpty()) {
				listOf(
					ItemReviewInAdvDetails(
						ItemUserInReviewsInAdvDetails(
							viewModel.userId,
							null,
							null,
							viewModel.user.name.orEmpty(),
							null,
							null,
							null,
							viewModel.user.image.orEmpty(),
							null,
						),
						0,
						getString(R.string.add_rate_2),
						""
					)
				)
			}else {
				response.reviews.orEmpty().take(3)
			}
			viewModel.adapterReviews.submitList(
				reviews
			)
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<MyAdsFragment.NewAdvertisementState> {
			if (it == MyAdsFragment.NewAdvertisementState.BECAME_PREMIUM) {
				viewModel.afterBecamePremium(this)
			}
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean>(AppConsts.KEY_BOOLEAN_1) {
			// Changed reviews
			handleRetryAbleActionOrGoBack(
				action = {
					viewModel.adsUseCase.getMyAdvertisementDetails(viewModel.args.id)
				}
			) {
				viewModel.response.value = it

				val reviews = if (it.reviews.isNullOrEmpty()) {
					listOf(
						ItemReviewInAdvDetails(
							ItemUserInReviewsInAdvDetails(
								viewModel.userId,
								null,
								null,
								viewModel.user.name.orEmpty(),
								null,
								null,
								null,
								viewModel.user.image.orEmpty(),
								null,
							),
							0,
							getString(R.string.add_rate_2),
							""
						)
					)
				}else {
					it.reviews.orEmpty().take(3)
				}
				viewModel.adapterReviews.submitList(
					reviews
				)
			}
		}
	}

}
