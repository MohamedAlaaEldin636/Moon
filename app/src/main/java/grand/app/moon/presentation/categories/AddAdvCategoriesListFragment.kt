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
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.showSnackbarWithAction
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.categories.viewModel.AddAdvCategoriesListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAdvCategoriesListFragment : BaseFragment<FragmentAddAdvCategoriesListBinding>()  {

	private val viewModel by viewModels<AddAdvCategoriesListViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_add_adv_categories_list

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.layoutManager = LinearLayoutManager(
			requireContext(), LinearLayoutManager.VERTICAL, false
		)
		binding.recyclerView.adapter = viewModel.adapter

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.showLoading.collect {
					if (it) {
						hideKeyboard()
						showLoading()
					}else {
						hideLoading()
					}
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.categories.collect {
					when (it) {
						is Resource.Success -> {
							viewModel.adapter.submitList(it.value.data.orEmpty())
						}
						is Resource.Failure -> binding.root.showSnackbarWithAction(it) {
							viewModel.getCategories()
						}
						else -> {}
					}
				}
			}
		}
	}

}
