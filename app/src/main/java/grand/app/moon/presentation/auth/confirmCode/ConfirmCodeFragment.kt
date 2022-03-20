package grand.app.moon.presentation.auth.confirmCode

import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.databinding.FragmentConfirmCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ConfirmCodeFragment : BaseFragment<FragmentConfirmCodeBinding>() {

  private val viewModel: ConfirmViewModel by viewModels()
  private lateinit var countDownTimer: CountDownTimer

  override
  fun getLayoutId() = R.layout.fragment_confirm_code

  override
  fun setBindingVariables() {
    binding.viewmodel = viewModel
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
            if (viewModel.request.type == "verify")
              openCountry()
            else
              openChangePassword()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it, retryAction = { viewModel.verifyAccount() })
          }
          Resource.Default -> {}
        }
      }
    }
    lifecycleScope.launchWhenResumed {
      viewModel.forgetResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            startTimer()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it, retryAction = { viewModel.verifyAccount() })
          }
          Resource.Default -> {}
        }
      }
    }
  }

  private fun openCountry() {
    navigateSafe(ConfirmCodeFragmentDirections.actionFragmentConfirmCodeToCountriesFragment())
  }

  private fun openChangePassword() {
    navigateSafe(ConfirmCodeFragmentDirections.actionFragmentConfirmCodeToChangePasswordFragment())
  }

  override fun onStart() {
    super.onStart()
    startTimer()
  }

  private fun startTimer() {
    countDownTimer = object : CountDownTimer(30000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        binding.tvForgetTimer.text = (millisUntilFinished / 1000).toString().plus(": 00")
      }

      override fun onFinish() {
        binding.tvLoginForget.isEnabled = true
      }
    }.start()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    countDownTimer.cancel()
  }
}