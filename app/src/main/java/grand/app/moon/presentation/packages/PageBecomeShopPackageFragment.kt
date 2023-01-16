package grand.app.moon.presentation.packages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.FragmentBecomeShopPackagesBinding
import grand.app.moon.databinding.FragmentPageBecomeShopPackageBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.packages.viewModel.BecomeShopPackagesViewModel
import grand.app.moon.presentation.packages.viewModel.PageBecomeShopPackageViewModel
import kotlin.math.roundToInt

@AndroidEntryPoint
class PageBecomeShopPackageFragment : BaseFragment<FragmentPageBecomeShopPackageBinding>() {

	companion object {
		const val ARGS_ITEM = "ARGS_ITEM"
	}

	private val viewModel by viewModels<PageBecomeShopPackageViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_page_become_shop_package

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		MyLogger.e("euiwhid -> $arguments")

		viewModel.response.value = arguments?.getString(ARGS_ITEM).fromJsonInlinedOrNull()
	}

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}
