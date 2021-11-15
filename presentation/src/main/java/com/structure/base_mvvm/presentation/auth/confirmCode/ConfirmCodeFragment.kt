package com.structure.base_mvvm.presentation.auth.confirmCode

import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.presentation.databinding.FragmentConfirmCodeBinding
import dagger.hilt.android.AndroidEntryPoint

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
    viewModel.backToPreviousScreen.observe(this) { backToPreviousScreen() }
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