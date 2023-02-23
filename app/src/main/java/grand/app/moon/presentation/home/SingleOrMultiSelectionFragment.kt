package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentSingleOrMultiSelectionBinding
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.SingleOrMultiSelectionViewModel

@AndroidEntryPoint
class SingleOrMultiSelectionFragment : BaseFragment<FragmentSingleOrMultiSelectionBinding>() {

	companion object {
		const val KEY_RESULT_SINGLE_SELECTION = "KEY_RESULT_SINGLE_SELECTION"
		const val KEY_RESULT_MULTI_SELECTION = "KEY_RESULT_MULTI_SELECTION"
	}

	private val viewModel by viewModels<SingleOrMultiSelectionViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_single_or_multi_selection

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
