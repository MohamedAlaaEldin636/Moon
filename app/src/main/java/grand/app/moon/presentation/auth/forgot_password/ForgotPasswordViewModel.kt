package grand.app.moon.presentation.auth.forgot_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.auth.entity.request.ForgetPasswordRequest
import grand.app.moon.domain.auth.use_case.ForgetPasswordUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
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