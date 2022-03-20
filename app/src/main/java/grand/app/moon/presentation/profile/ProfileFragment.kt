package grand.app.moon.presentation.profile

import androidx.fragment.app.viewModels
import grand.app.moon.presentation.base.utils.Constants
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.databinding.FragmentProfileBinding
import grand.app.moon.presentation.profile.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

  private val viewModel: ProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_profile

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) {
      if (it == Constants.CONFIRM_CODE)
        openConfirmCode()
    }
    viewModel.backToPreviousScreen.observe(this) { backToPreviousScreen() }
  }

  private fun openConfirmCode() {
//    navigateSafe(SignUpFragmentDirections.actionOpenConfirmCodeFragment())
  }
}