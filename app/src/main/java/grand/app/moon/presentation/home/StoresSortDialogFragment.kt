package grand.app.moon.presentation.home

import android.view.Gravity
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.DialogFragmentStoresSortBinding
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.orStringNullIfNullOrEmpty
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.base.extensions.getMyDrawable
import grand.app.moon.presentation.home.viewModels.StoresSortViewModel

@AndroidEntryPoint
class StoresSortDialogFragment : MADialogFragment<DialogFragmentStoresSortBinding>() {

	companion object {
		fun launch(navController: NavController, title: String, sortBy: AllStoresFragment.SortBy?) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.stores.sort.dialog",
				paths = arrayOf(title, sortBy?.name.orStringNullIfNullOrEmpty())
			)
		}
	}

	private val viewModel by viewModels<StoresSortViewModel>()

	override fun getLayoutId(): Int = R.layout.dialog_fragment_stores_sort

	override val windowGravity: Int = Gravity.BOTTOM

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(getMyDrawable(R.drawable.dr_top_20))
	}

	override fun initializeBindingVariables() {
		binding.viewModel = viewModel
	}

}
