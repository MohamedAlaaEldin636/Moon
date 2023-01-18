package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMakeAdvOrStorePremiumBinding
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
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
			viewModel.getCurrentlyUsedAdapter(),
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

		viewModel.adsNotStoresAreSelected.observe(viewLifecycleOwner) {
			(activity as? HomeActivity)?.binding?.toolbar?.title = if (it == true) {
				getString(R.string.ads_promotion_87)
			}else {
				getString(R.string.store_promotion_87)
			}
		}
	}

}
