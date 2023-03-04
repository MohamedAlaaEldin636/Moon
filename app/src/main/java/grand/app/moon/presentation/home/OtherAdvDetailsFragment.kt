package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentOtherAdvDetailsBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.OtherAdvDetailsViewModel
import grand.app.moon.presentation.myAds.model.ItemReviewInAdvDetails
import grand.app.moon.presentation.myAds.model.ItemUserInReviewsInAdvDetails

@AndroidEntryPoint
class OtherAdvDetailsFragment : BaseFragment<FragmentOtherAdvDetailsBinding>() {

	companion object {
		fun launch(navController: NavController, id: Int) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.other.adv.details",
				paths = arrayOf(id.toString())
			)
		}
	}

	private val viewModel by viewModels<OtherAdvDetailsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_other_adv_details

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoShop.getAdvDetailsFromView(viewModel.args.id)
			}
		) { response ->
			viewModel.response.value = response

			viewModel.adapterImages.submitList(
				response.images.orEmpty().map { it.image.orEmpty() }
			)
			_binding?.sliderView?.post {
				_binding?.sliderView?.setSliderAdapter(viewModel.adapterImages)
				_binding?.sliderView?.startAutoCycle()
			}

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
							kotlin.runCatching { viewModel.userLocalUseCase().id }.getOrNull(),
							null,
							null,
							kotlin.runCatching { viewModel.userLocalUseCase().name.orEmpty() }.getOrElse { getString(R.string.name) },
							null,
							null,
							null,
							kotlin.runCatching { viewModel.userLocalUseCase().image.orEmpty() }.getOrNull(),
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

			viewModel.adapterAds.submitList(
				response.similarAds.orEmpty()
			)

			viewModel.adapterStores.submitList(
				response.similarStores.orEmpty()
			)
		}
	}

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.sliderView.post {
			binding.sliderView.setSliderAdapter(viewModel.adapterImages)
			binding.sliderView.startAutoCycle()
		}

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

		binding.recyclerViewAds.setupWithRVItemCommonListUsage(
			viewModel.adapterAds,
			true,
			1
		) { layoutParams ->
			val number = 2
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = totalWidth / number
		}

		binding.recyclerViewStores.setupWithRVItemCommonListUsage(
			viewModel.adapterStores,
			true,
			1
		) { layoutParams ->
			val number = 2
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = totalWidth / number

			MyLogger.e("hhhhhhhhh -> ${layoutParams.height}")
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

	enum class SocialMedia {
		FACEBOOK, INSTAGRAM, TWITTER, YOUTUBE, COPY_LINK
	}

}