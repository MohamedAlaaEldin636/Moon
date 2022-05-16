package grand.app.moon.presentation.auth.confirmCode

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.facebook.login.LoginFragment
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.auth.use_case.VerifyAccountUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.sendFirebaseSMS
import grand.app.moon.core.extenstions.showError
import grand.app.moon.core.extenstions.verifyCode
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.presentation.auth.log_in.LogInFragmentDirections
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
  private val logInUseCase: LogInUseCase,
  val userLocalUseCase: UserLocalUseCase,
  private val verifyAccountUseCase: VerifyAccountUseCase) :
  BaseViewModel() {
  val request = VerifyAccountRequest()
  private val _verifyResponse = MutableStateFlow<Resource<BaseResponse<User>>>(Resource.Default)
  val verifyResponse = _verifyResponse

  val _sendCode = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val sendCode = _sendCode

  init {
    verifyAccountUseCase.baseViewModel = this
    startTimer()
  }
  fun verifyAccount(v: View) {
    verifyAccountUseCase(request)
      .onEach { result ->
        _verifyResponse.value = result
      }
      .launchIn(viewModelScope)
//    v.context.verifyCode(v,request.verificationId,request.code) {
//      if (it) {
//        logInUseCase(request)
//          .onEach { result ->
//            _verifyResponse.value = result
//          }
//          .launchIn(viewModelScope)
//      }
//    }
  }

  fun resendCode() {
    Log.d(TAG, "resend: heererer")
    verifyAccountUseCase.sendCode(request)
      .onEach { result ->
        _sendCode.value = result
      }
      .launchIn(viewModelScope)
  }

  fun resend(v: View) {
    Log.d(TAG, "resend: ")
    resend = false
    resendCode()
    countDownTimer.start()
    notifyChange()

//    val fragment = v.findFragment<LoginFragment>()
//    v.context.sendFirebaseSMS(fragment.requireActivity(),v,request.country_code+request.phone) { verificationId ->
//     request.verificationId = verificationId
//    }
  }


  var timerText = "60:00"
  var resend = false
  lateinit var countDownTimer: CountDownTimer
  private val TAG = "ConfirmViewModel"
  private fun startTimer() {
    Log.d(TAG, "startTimer: ")
    countDownTimer = object : CountDownTimer(10000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        timerText = when {
          (millisUntilFinished / 60000) < 10 -> "0" + (millisUntilFinished / 1000)
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