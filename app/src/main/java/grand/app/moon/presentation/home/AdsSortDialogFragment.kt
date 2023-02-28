package grand.app.moon.presentation.home

import android.view.Gravity
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.DialogFragmentAdsSortBinding
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.orStringNullIfNullOrEmpty
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.base.extensions.getMyDrawable
import grand.app.moon.presentation.home.viewModels.AdsSortViewModel

@AndroidEntryPoint
class AdsSortDialogFragment : MADialogFragment<DialogFragmentAdsSortBinding>() {

	companion object {
		fun launch(navController: NavController, title: String, sortBy: FilterAllFragment.SortBy?) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.ads.sort.dialog",
				paths = arrayOf(title, sortBy?.name.orStringNullIfNullOrEmpty())
			)
		}
	}

	private val viewModel by viewModels<AdsSortViewModel>()

	override fun getLayoutId(): Int = R.layout.dialog_fragment_ads_sort

	override val windowGravity: Int = Gravity.BOTTOM

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(getMyDrawable(R.drawable.dr_top_20))
	}

	override fun initializeBindingVariables() {
		binding.viewModel = viewModel
	}

}
