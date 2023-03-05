package grand.app.moon.presentation.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.DialogFragmentReportingBinding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.home.viewModels.ReportingViewModel

@AndroidEntryPoint
class ReportingDialogFragment : MADialogFragment<DialogFragmentReportingBinding>() {

	companion object {
		fun launch(navController: NavController, type: Type, id: Int) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.reporting",
				paths = arrayOf(type.toString(), id.toString())
			)
		}
	}

	private val viewModel by viewModels<ReportingViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				when (viewModel.args.type) {
					Type.REPORT_ADS -> viewModel.repoShop.getAdvReportingReason()
					Type.REPORT_STORES -> viewModel.repoShop.getStoreReportingReasons()
					Type.BLOCK_STORES -> viewModel.repoShop.getStoreBlockingReasons()
				}
			}
		) { response ->
			viewModel.adapter.submitList(response)
		}
	}

	override fun getLayoutId(): Int = R.layout.dialog_fragment_reporting

	override val windowGravity: Int = Gravity.BOTTOM

	override val heightIsMatchParent: Boolean = true

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
	}

	override fun initializeBindingVariables() {
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

	enum class Type {
		REPORT_ADS, REPORT_STORES, BLOCK_STORES
	}

}
