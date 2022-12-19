package grand.app.moon.presentation.home

import android.graphics.drawable.InsetDrawable
import android.view.Window
import androidx.annotation.CallSuper
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.DialogFragmentAppGlobalAnnouncementBinding
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.home.viewModels.AppGlobalAnnouncementViewModel
import kotlin.math.roundToInt

@AndroidEntryPoint
class AppGlobalAnnouncementDialogFragment : MADialogFragment<DialogFragmentAppGlobalAnnouncementBinding>() {

	override fun getLayoutId(): Int = R.layout.dialog_fragment_app_global_announcement

	override val canceledOnTouchOutside: Boolean = false

	private val viewModel by viewModels<AppGlobalAnnouncementViewModel>()

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		val drawable = InsetDrawable(
			AppCompatResources.getDrawable(requireContext(), R.drawable.dr_rounded_global_announcement),
			requireContext().dpToPx(16f).roundToInt()
		)
		window.setBackgroundDrawable(drawable)
	}

	override fun initializeBindingVariables() {
		binding.viewModel = viewModel
	}

}
