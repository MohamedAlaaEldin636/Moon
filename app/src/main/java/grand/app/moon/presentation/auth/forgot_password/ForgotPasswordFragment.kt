package grand.app.moon.presentation.auth.forgot_password

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import com.structure.base_mvvm.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {
  private val viewModel: ForgotPasswordViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_forgot_password

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(this) { backToPreviousScreen() }
    lifecycleScope.launchWhenResumed {
      viewModel.forgetPasswordResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            openConfirmCode()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it, retryAction = { viewModel.sendCode() })
          }
          Resource.Default -> {}
        }
      }
    }
  }

  private fun openConfirmCode() {
    navigateSafe(
      ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToFragmentConfirmCode(
        viewModel.request.email,
        Constants.FORGET
      )
    )
  }
}