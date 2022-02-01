package app.grand.tafwak.presentation.auth.forgot_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.auth.entity.request.ForgetPasswordRequest
import app.grand.tafwak.domain.auth.use_case.ForgetPasswordUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
  private val forgetPasswordUseCase: ForgetPasswordUseCase,
  savedStateHandle: SavedStateHandle
) :
  BaseViewModel() {
  var request = ForgetPasswordRequest()
  private val _forgetPasswordResponse =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val forgetPasswordResponse = _forgetPasswordResponse
  init {
    savedStateHandle.get<String>("email")?.let { email ->
      request.email = email
    }

  }
  fun sendCode() {
    request.type = Constants.FORGET
    forgetPasswordUseCase(request)
      .onEach { result ->
        _forgetPasswordResponse.value = result
      }
      .launchIn(viewModelScope)
  }
}