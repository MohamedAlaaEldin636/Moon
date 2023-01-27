package grand.app.moon.presentation.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentGeneralSettingsBinding
import grand.app.moon.databinding.FragmentMoreBinding
import grand.app.moon.databinding.FragmentMyAccount2Binding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment

@AndroidEntryPoint
class GeneralSettingsFragment : BaseFragment<FragmentGeneralSettingsBinding>() {

	val viewModel by viewModels<GeneralSettingsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_general_settings

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
