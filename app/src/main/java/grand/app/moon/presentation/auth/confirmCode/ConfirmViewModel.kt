package grand.app.moon.presentation.auth.confirmCode

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.auth.use_case.VerifyAccountUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val verifyAccountUseCase: VerifyAccountUseCase) :
  BaseViewModel() {
  val request = VerifyAccountRequest()
  private val _verifyResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val verifyResponse = _verifyResponse

  val _sendCode = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val sendCode = _sendCode

  init {
    verifyAccountUseCase.baseViewModel = this
    startTimer()
  }
  fun verifyAccount() {
    verifyAccountUseCase(request)
      .onEach { result ->
        _verifyResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun resendCode() {
    Log.d(TAG, "resend: heererer")
    verifyAccountUseCase.sendCode(request)
      .onEach { result ->
        _sendCode.value = result
      }
      .launchIn(viewModelScope)
  }

  fun resend() {
    //call api
    Log.d(TAG, "resend: ")
    resend = false
    resendCode()
    countDownTimer.start()
  }


  var timerText = "60:00"
  var resend = false
  lateinit var countDownTimer: CountDownTimer
  private val TAG = "ConfirmViewModel"
  private fun startTimer() {
    Log.d(TAG, "startTimer: ")
    countDownTimer = object : CountDownTimer(60000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        timerText = when {
          (millisUntilFinished / 1000) < 10 -> "0" + (millisUntilFinished / 1000)
          else -> (millisUntilFinished / 1000)
        }.toString().plus(" : 00")
        notifyChange()
        Log.d(TAG, "onTick: $timerText")
      }

      override fun onFinish() {
        resend = true
        Log.d(TAG, "onFinish: resend")
        notifyChange()
      }
    }.start()
  }

  override fun onCleared() {
    Log.d(TAG, "onCleared: cancel")
    countDownTimer.cancel()
    super.onCleared()
  }



}