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
import grand.app.moon.extensions.handleRetryAbleActionCancellable
import grand.app.moon.extensions.makeStatusBarToPrimaryDarkWithWhiteIcons
import grand.app.moon.extensions.makeStatusBarTransparentWithWhiteIcons
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
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
		/*binding.sliderView.post {
			binding.sliderView.isAutoCycle = false

			binding.sliderView.setIndicatorEnabled(true)
			binding.sliderView.indicatorSelectedColor = requireContext().getColor(R.color.colorPrimaryDark)
			binding.sliderView.indicatorUnselectedColor = requireContext().getColor(R.color.colorPrimaryLight)
		}*/

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
			viewModel.adapterProperties,
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

			viewModel.adapterReviews.submitList(
				response.reviews.orEmpty()
			)
		}
	}

}
