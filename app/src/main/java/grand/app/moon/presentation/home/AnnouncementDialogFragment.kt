package grand.app.moon.presentation.home

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Window
import androidx.annotation.CallSuper
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.DialogFragmentAnnouncementBinding
import grand.app.moon.databinding.DialogFragmentAppGlobalAnnouncementBinding
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.home.viewModels.AnnouncementViewModel
import grand.app.moon.presentation.home.viewModels.AppGlobalAnnouncementViewModel
import kotlin.math.roundToInt

@AndroidEntryPoint
class AnnouncementDialogFragment : MADialogFragment<DialogFragmentAnnouncementBinding>() {

	override fun getLayoutId(): Int = R.layout.dialog_fragment_announcement

	override val canceledOnTouchOutside: Boolean = false

	override val heightIsMatchParent: Boolean = true

	private val viewModel by viewModels<AnnouncementViewModel>()

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(
			ColorDrawable(
				ContextCompat.getColor(requireContext(), R.color.announcement_dialog_window_background)
			)
		)
	}

	override fun initializeBindingVariables() {
		binding.viewModel = viewModel
	}

}
