package grand.app.moon.presentation.auth.confirmCode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.auth.entity.request.ForgetPasswordRequest
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.auth.use_case.ForgetPasswordUseCase
import grand.app.moon.domain.auth.use_case.VerifyAccountUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
  private val verifyAccountUseCase: VerifyAccountUseCase,
  private val forgetPasswordUseCase: ForgetPasswordUseCase,
  savedStateHandle: SavedStateHandle
) :
  BaseViewModel() {
  val request = VerifyAccountRequest()
  private val _verifyResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val verifyResponse = _verifyResponse
  private val forgetPasswordRequest = ForgetPasswordRequest()
  private val _forgetResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val forgetResponse = _forgetResponse

  init {
    savedStateHandle.get<String>("email")?.let { email ->
      request.email = email
      forgetPasswordRequest.email = email
    }
    savedStateHandle.get<String>("type")?.let { type ->
      request.type = type
      forgetPasswordRequest.type = type
    }
  }

  fun verifyAccount() {
    verifyAccountUseCase(request)
      .onEach { result ->
        _verifyResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun resendCode() {
    forgetPasswordUseCase(forgetPasswordRequest)
      .onEach { result ->
        _forgetResponse.value = result
      }
      .launchIn(viewModelScope)
  }

}