package grand.app.moon.presentation.myAds

import android.os.Bundle
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMyAdOrShopPackageBinding
import grand.app.moon.extensions.handleRetryAbleActionCancellable
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.MyAdOrShopPackageViewModel

@AndroidEntryPoint
class MyAdOrShopPackageFragment : BaseFragment<FragmentMyAdOrShopPackageBinding>() {

	private val viewModel by viewModels<MyAdOrShopPackageViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (viewModel.response.value == null) {
			handleRetryAbleActionCancellable(
				action = {
					if (viewModel.args.isAdvNotShop) {
						viewModel.repoPackages.getMyPackageForPremiumAds()
					}else {
						viewModel.repoPackages.getMyPackageForPremiumShops()
					}
				}
			) {
				viewModel.response.value = it
			}
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_my_ad_or_shop_package

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}
