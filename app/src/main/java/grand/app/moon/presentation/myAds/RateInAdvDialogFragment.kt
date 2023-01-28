package grand.app.moon.presentation.myAds

import android.view.Gravity
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.DialogFragmentRateInAdvBinding
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.base.extensions.getMyDrawable
import grand.app.moon.presentation.myAds.viewModel.RateInAdvViewModel

@AndroidEntryPoint
class RateInAdvDialogFragment : MADialogFragment<DialogFragmentRateInAdvBinding>() {

	private val viewModel by viewModels<RateInAdvViewModel>()

	override fun getLayoutId(): Int = R.layout.dialog_fragment_rate_in_adv

	override val windowGravity: Int = Gravity.BOTTOM

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(getMyDrawable(R.drawable.dr_top_20))
	}

	override fun initializeBindingVariables() {
		binding?.viewModel = viewModel
	}

}
