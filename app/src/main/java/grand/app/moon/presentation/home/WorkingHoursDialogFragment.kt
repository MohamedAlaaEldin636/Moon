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
import grand.app.moon.databinding.DialogFragmentWorkingHoursBinding
import grand.app.moon.domain.shop.ResponseWorkingHour
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.home.viewModels.WorkingHoursDialogViewModel

@AndroidEntryPoint
class WorkingHoursDialogFragment : MADialogFragment<DialogFragmentWorkingHoursBinding>() {

	companion object {
		fun launch(navController: NavController, workingHours: List<ResponseWorkingHour>) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.dialog.fragment.working.hours",
				paths = arrayOf(workingHours.toJsonInlinedOrNull().orStringNullIfNullOrEmpty())
			)
		}
	}

	private val viewModel by viewModels<WorkingHoursDialogViewModel>()

	override fun getLayoutId(): Int = R.layout.dialog_fragment_working_hours

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

}
