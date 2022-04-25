package grand.app.moon.presentation.auth.confirmCode

import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.navigateSafe
import grand.app.moon.databinding.FragmentConfirmCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.home.HomeActivity
import kotlinx.coroutines.flow.collect
import com.onesignal.OneSignal




@AndroidEntryPoint
class ConfirmCodeFragment : BaseFragment<FragmentConfirmCodeBinding>() {
  val args: ConfirmCodeFragmentArgs by navArgs()

  private val viewModel: ConfirmViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_confirm_code

  override
  fun setBindingVariables() {
    binding.viewmodel = viewModel
    viewModel.request.phone = args.phone
    viewModel.request.type = args.type
  }

  override
  fun setupObservers() {
    lifecycleScope.launchWhenResumed {
      viewModel.verifyResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            makeIntegrationWithRedirectHome(viewModel.userLocalUseCase.invoke().id)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    lifecycleScope.launchWhenResumed {
      viewModel._sendCode.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }


  override fun onStart() {
    super.onStart()
  }

  override fun onDestroyView() {
    super.onDestroyView()
//    countDownTimer.cancel()
  }
}