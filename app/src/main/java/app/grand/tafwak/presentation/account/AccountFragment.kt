package app.grand.tafwak.presentation.account

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.auth.AuthActivity
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.*
import com.structure.base_mvvm.R
import com.structure.base_mvvm.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>() {

  private val viewModelTeacher: AccountViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_account

  override
  fun setBindingVariables() {
    binding.viewModel = viewModelTeacher
  }

  override
  fun setupObservers() {
    viewModelTeacher.clickEvent.observe(this) { showLogOutPopUp() }
    lifecycleScope.launchWhenResumed {
      viewModelTeacher.logOutResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            openLogInScreen()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }

  private fun showLogOutPopUp() {
    PrettyPopUpHelper.Builder(childFragmentManager)
      .setStyle(PrettyPopUpHelper.Style.STYLE_1_HORIZONTAL_BUTTONS)
      .setTitle(R.string.log_out)
      .setTitleColor(getMyColor(R.color.colorPrimaryDark))
      .setContent(R.string.log_out_hint)
      .setContentColor(getMyColor(R.color.gray))
      .setPositiveButtonBackground(R.color.colorPrimaryDark)
      .setPositiveButtonTextColor(getMyColor(R.color.white))
      .setImage(R.drawable.ic_logout)
      .setPositiveButton(R.string.log_out) {
        it.dismiss()
        viewModelTeacher.logOut()
      }
      .setNegativeButtonBackground(R.drawable.btn_gray)
      .setNegativeButtonTextColor(getMyColor(R.color.white))
      .setNegativeButton(getMyString(R.string.cancel), null)
      .create()
  }

  private fun openLogInScreen() {
    requireActivity().openActivityAndClearStack(AuthActivity::class.java)
  }
}