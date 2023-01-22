package grand.app.moon.presentation.packages

import android.os.Bundle
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.FragmentBecomeShopPackagesBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.packages.viewModel.BecomeShopPackagesViewModel
import kotlin.math.roundToInt

@AndroidEntryPoint
class BecomeShopPackagesFragment : BaseFragment<FragmentBecomeShopPackagesBinding>() {

	val viewModel by viewModels<BecomeShopPackagesViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_become_shop_packages

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		getPackages()
	}

	private fun getPackages() {
		handleRetryAbleActionCancellable(
			action = {
				viewModel.repoPackages.getBecomeShopPackagesSuspend()
			},
			hideLoadingAction = {}
		) {
			viewModel.currentResponsePagination = it
			if (it.data.isNullOrEmpty().not()) {
				viewModel.allPackages = viewModel.allPackages + it.data.orEmpty()
				val pagerAdapter = viewModel.getPagerAdapter(this)
				pagerAdapter.notifyItemRangeInserted(pagerAdapter.itemCount, it.data.orEmpty().size)
				viewModel.changePageIndicatorsSize(viewModel.allPackages.size)
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			true,
			1
		)
		binding.recyclerView.addUniqueTypeItemDecoration(
			MADividerItemDecoration(horizontalSpacing = requireContext().dpToPx(11f).roundToInt())
		)

		binding.viewPager2.isSaveEnabled = false
		kotlin.runCatching {
			binding.viewPager2.setPageTransformer(DepthPageTransformer2(
				requireContext().dpToPx(48f - 8f/*was 64 try 32*/).roundToInt(),
				requireContext().dpToPx(33f).roundToInt(),
				additionalOffsetPx = requireContext().dpToPx(24f)
			))
		}.getOrElse {
			MyLogger.e("error vpppp -> $it")
		}
		binding.viewPager2.offscreenPageLimit = 3
		binding.viewPager2.adapter = viewModel.getPagerAdapter(this)
		binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				viewModel.changeSelectedPageIndicator(position)

				if (
					viewModel.allPackages.size.dec() - position <=
					viewModel.currentResponsePagination?.meta?.oneThirdPerPage.orZero()
					&& viewModel.currentResponsePagination?.links?.next.isNullOrEmpty().not()
				) {
					handleRetryAbleActionCancellable(
						action = {
							viewModel.repoPackages.getBecomeShopPackagesSuspend(
								viewModel.currentResponsePagination?.meta?.currentPage.orZero().inc()
							)
						}
					) {
						viewModel.currentResponsePagination = it
						if (it.data.isNullOrEmpty().not()) {
							viewModel.allPackages = viewModel.allPackages + it.data.orEmpty()
							val pagerAdapter = viewModel.getPagerAdapter(this@BecomeShopPackagesFragment)
							pagerAdapter.notifyItemRangeInserted(pagerAdapter.itemCount, it.data.orEmpty().size)
							viewModel.changePageIndicatorsSize(viewModel.allPackages.size)
						}
					}
				}
			}
		})
		binding.viewPager2.visibility = View.INVISIBLE
		showLoading()
		binding.viewPager2.postDelayed(500) {
			binding.viewPager2.setCurrentItem(1, true)
			binding.viewPager2.postDelayed(500) {
				/*val index = viewModel.allPackages.indexOfFirst {
					it.isSubscribed == true
				}.let {
					if (it == -1) 0 else it
				}*/

				binding.viewPager2.setCurrentItem(0, true)
				binding.viewPager2.visibility = View.VISIBLE

				if (viewModel.allPackages.isNotEmpty()) {
					hideLoading()
				}
			}
		}

		observeCreateShopFragment()
	}

	fun observeCreateShopFragment() {
		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			if (it) {
				hideLoading()

				findNavController().navigateUp()
			}
		}
	}

}
