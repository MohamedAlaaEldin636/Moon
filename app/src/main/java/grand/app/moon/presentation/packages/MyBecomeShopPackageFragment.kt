package grand.app.moon.presentation.packages

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMyBecomeShopPackageBinding
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.packages.viewModel.MyBecomeShopPackageViewModel

@AndroidEntryPoint
class MyBecomeShopPackageFragment : BaseFragment<FragmentMyBecomeShopPackageBinding>() {

	private val viewModel by viewModels<MyBecomeShopPackageViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_my_become_shop_package

	// todo call api to get my current package as it can be called from home screen in add advertisement isa.

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}
