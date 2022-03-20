package grand.app.moon.presentation.auth.sign_up

import android.widget.CompoundButton
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.auth.entity.request.RegisterRequest
import grand.app.moon.domain.auth.use_case.RegisterUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
  BaseViewModel() {
  val registerRequest = RegisterRequest()
  private val _registerResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val registerResponse = _registerResponse
  var validationException = SingleLiveEvent<Int>()


  fun onCheckedChange(button: CompoundButton?, check: Boolean) {
    registerRequest.isAccept = check
  }

  fun register() {
    registerUseCase(registerRequest)
      .catch { exception -> validationException.value = exception.message?.toInt() }
      .onEach { result ->
        _registerResponse.value = result
      }
      .launchIn(viewModelScope)
  }
}