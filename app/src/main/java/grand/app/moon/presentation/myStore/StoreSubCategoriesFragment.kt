package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentStoreCategoriesBinding
import grand.app.moon.databinding.FragmentStoreSubCategoriesBinding
import grand.app.moon.extensions.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.StoreCategoriesViewModel
import grand.app.moon.presentation.myStore.viewModel.StoreSubCategoriesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreSubCategoriesFragment : BaseFragment<FragmentStoreSubCategoriesBinding>() {

	private val viewModel by viewModels<StoreSubCategoriesViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_store_sub_categories

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter.withDefaultHeaderAndFooterAdapters(),
			false,
			1
		)

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.subCategories.collectLatest {
					viewModel.adapter.submitData(it)
				}
			}
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			if (it) {
				viewModel.adapter.refresh()
			}
		}
	}

}
