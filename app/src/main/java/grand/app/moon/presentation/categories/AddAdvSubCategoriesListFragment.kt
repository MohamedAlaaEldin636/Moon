package grand.app.moon.presentation.categories

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAddAdvCategoriesListBinding
import grand.app.moon.databinding.FragmentAddAdvSubCategoriesListBinding
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.showSnackbarWithAction
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.categories.viewModel.AddAdvCategoriesListViewModel
import grand.app.moon.presentation.categories.viewModel.AddAdvSubCategoriesListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAdvSubCategoriesListFragment : BaseFragment<FragmentAddAdvSubCategoriesListBinding>()  {

	private val viewModel by viewModels<AddAdvSubCategoriesListViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_add_adv_sub_categories_list

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.layoutManager = LinearLayoutManager(
			requireContext(), LinearLayoutManager.VERTICAL, false
		)
		binding.recyclerView.adapter = viewModel.adapter
	}

}
