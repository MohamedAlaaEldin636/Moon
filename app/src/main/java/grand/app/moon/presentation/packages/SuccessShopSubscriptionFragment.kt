package grand.app.moon.presentation.packages

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentSuccessShopSubscriptionBinding
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.packages.viewModel.SuccessShopSubscriptionViewModel

@AndroidEntryPoint
class SuccessShopSubscriptionFragment : BaseFragment<FragmentSuccessShopSubscriptionBinding>() {

	val viewModel by viewModels<SuccessShopSubscriptionViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_success_shop_subscription

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}
