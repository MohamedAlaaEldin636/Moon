package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentCategoryDetails2Binding
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.CategoryDetails2ViewModel

@AndroidEntryPoint
class CategoryDetails2Fragment : BaseFragment<FragmentCategoryDetails2Binding>() {

	companion object {
		fun launch(navController: NavController, categoryId: Int, categoryName: String) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.category.details.two",
				paths = arrayOf(categoryId.toString(), categoryName)
			)
		}
	}

	private val viewModel by viewModels<CategoryDetails2ViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_category_details_2

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// todo setups
	}

}
