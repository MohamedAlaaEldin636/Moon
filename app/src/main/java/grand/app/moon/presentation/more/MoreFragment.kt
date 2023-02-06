package grand.app.moon.presentation.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.databinding.FragmentMoreBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {

	val viewModel by viewModels<MoreViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		checkRestDaysInPackage()
	}

	override fun getLayoutId(): Int = R.layout.fragment_more

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			viewModel.user.value = viewModel.userLocalUseCase()

			checkRestDaysInPackage()
		}

		viewModel.user.value = viewModel.userLocalUseCase()
	}

	private fun checkRestDaysInPackage() {
		MyLogger.e("requireContext().isLogin() && viewModel.user.value?.isStore.orFalse() ${requireContext().isLogin()} ${viewModel.user.value?.isStore.orFalse()}")
		if (requireContext().isLogin() && viewModel.user.value?.isStore.orFalse()) {
			handleRetryAbleActionOrGoBack(
				action = {
					viewModel.repoPackages.getMyPackageOfBeingShop()
				}
			) {
				viewModel.restDaysInPackage.value = it.restDays.orZero()
			}
		}else {
			viewModel.restDaysInPackage.value = 0
		}
	}

}
