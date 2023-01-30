package grand.app.moon.presentation.packages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMyBecomeShopPackageBinding
import grand.app.moon.extensions.AppConsts
import grand.app.moon.extensions.handleRetryAbleActionCancellable
import grand.app.moon.extensions.navUpThenSetResultInBackStackEntrySavedStateHandleViaGson
import grand.app.moon.extensions.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.packages.viewModel.MyBecomeShopPackageViewModel

@AndroidEntryPoint
class MyBecomeShopPackageFragment : BaseFragment<FragmentMyBecomeShopPackageBinding>() {

	private val viewModel by viewModels<MyBecomeShopPackageViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (viewModel.response.value == null) {
			handleRetryAbleActionCancellable(
				action = {
					viewModel.repoPackages.getMyPackageOfBeingShop()
				}
			) {
				viewModel.response.value = it
			}
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_my_become_shop_package

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.name.observe(viewLifecycleOwner) {
			(activity as? HomeActivity)?.binding?.toolbar?.title = it
		}

		if (findNavController().previousBackStackEntry?.destination?.id != R.id.dest_become_shop_packages) {
			findNavController().currentBackStackEntry?.savedStateHandle?.remove<String>(AppConsts.NavController.GSON_KEY)

			observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
				if (it) {
					hideLoading()

					findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
						true // success package
					)
				}
			}
		}
	}

}
