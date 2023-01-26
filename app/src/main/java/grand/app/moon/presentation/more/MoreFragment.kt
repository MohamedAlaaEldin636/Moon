package grand.app.moon.presentation.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMoreBinding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {

	val viewModel by viewModels<MoreViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (viewModel.user.isStore.orFalse()) {
			handleRetryAbleActionOrGoBack(
				action = {
					viewModel.repoPackages.getMyPackageOfBeingShop()
				}
			) {
				viewModel.restDaysInPackage.value = it.restDays.orZero()
			}
		}
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
	}

}
