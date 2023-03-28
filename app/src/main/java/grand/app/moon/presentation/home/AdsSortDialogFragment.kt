package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.DialogFragmentAdsSortBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.base.extensions.getMyDrawable
import grand.app.moon.presentation.home.viewModels.AdsSortViewModel

@AndroidEntryPoint
class AdsSortDialogFragment : MADialogFragment<DialogFragmentAdsSortBinding>() {

	companion object {
		const val FRAGMENT_RESULT_ON_EITHER_CANCEL_OR_DISMISS = "FRAGMENT_RESULT_ON_EITHER_CANCEL_OR_DISMISS"

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

	override fun onEitherCancelOrDismissOnce() {
		MyLogger.e("djoaisjdoasjdao 1")

		setFragmentResultUsingJson(
			FRAGMENT_RESULT_ON_EITHER_CANCEL_OR_DISMISS, true
		)
	}

}
