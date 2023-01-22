package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAddOrEditStoreCategoryBinding
import grand.app.moon.databinding.FragmentAddOrEditStoreSubCategoryBinding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.myStore.viewModel.AddOrEditStoreCategoryViewModel
import grand.app.moon.presentation.myStore.viewModel.AddOrEditStoreSubCategoryViewModel

@AndroidEntryPoint
class AddOrEditStoreSubCategoryFragment : BaseFragment<FragmentAddOrEditStoreSubCategoryBinding>() {

	private val viewModel by viewModels<AddOrEditStoreSubCategoryViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoShop.getMyCategoriesInAllPagesOfPagination()
			}
		) {
			viewModel.allCategories.value = it
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_add_or_edit_store_sub_category

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		(activity as? HomeActivity)?.binding?.toolbar?.title = if (viewModel.args.id == null) {
			getString(R.string.add_new_sub_category)
		}else {
			getString(R.string.edit_sub_category)
		}
	}

}
