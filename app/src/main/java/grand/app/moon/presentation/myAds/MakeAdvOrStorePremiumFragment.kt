package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMakeAdvOrStorePremiumBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.myAds.viewModel.MakeAdvOrStorePremiumViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MakeAdvOrStorePremiumFragment : BaseFragment<FragmentMakeAdvOrStorePremiumBinding>() {

	private val viewModel by viewModels<MakeAdvOrStorePremiumViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_make_adv_or_store_premium

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.getCurrentlyUsedAdapter().withDefaultHeaderAndFooterAdapters(),
			true,
			1
		) { layoutParams ->
			layoutParams.width = width / 3
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					viewModel.adsPackages.collectLatest {
						viewModel.adapterForAds.submitData(it)
					}
				}

				launch {
					viewModel.shopsPackages.collectLatest {
						viewModel.adapterForShops.submitData(it)
					}
				}
			}
		}

		observeAdapterOnDataFirstLoadedOnceUsingViewLifecycle(viewModel.adapterForAds) {
			val list = viewModel.adapterForAds.snapshot()
			val id = list.firstOrNull {
				it?.isSubscribed.orFalse()
			}?.id ?: list.firstOrNull()?.id ?: return@observeAdapterOnDataFirstLoadedOnceUsingViewLifecycle
			val index = list.indexOfFirst { it?.id == id }

			viewModel.selectedAdsPackageId.value = id
			if (index != -1) {
				viewModel.adapterForAds.notifyItemChanged(index)
			}
		}
		observeAdapterOnDataFirstLoadedOnceUsingViewLifecycle(viewModel.adapterForShops) {
			val list = viewModel.adapterForShops.snapshot()
			val id = list.firstOrNull {
				it?.isSubscribed.orFalse()
			}?.id ?: list.firstOrNull()?.id ?: return@observeAdapterOnDataFirstLoadedOnceUsingViewLifecycle
			val index = list.indexOfFirst { it?.id == id }

			viewModel.selectedShopsPackageId.value = id
			if (index != -1) {
				viewModel.adapterForShops.notifyItemChanged(index)
			}
		}

		viewModel.adsNotStoresAreSelected.observe(viewLifecycleOwner) {
			(activity as? HomeActivity)?.binding?.toolbar?.title = if (it == true) {
				getString(R.string.ads_promotion_87)
			}else {
				getString(R.string.store_promotion_87)
			}
		}
	}

}
