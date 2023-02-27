package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAllCategoriesBinding
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.AllCategoriesViewModel

@AndroidEntryPoint
class AllCategoriesFragment : BaseFragment<FragmentAllCategoriesBinding>() {

	private val viewModel by viewModels<AllCategoriesViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_all_categories

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			3
		)

		viewModel.filteredCategories.distinctUntilChanged().observe(viewLifecycleOwner) {
			viewModel.adapter.submitList(it.orEmpty())
		}
	}

}
