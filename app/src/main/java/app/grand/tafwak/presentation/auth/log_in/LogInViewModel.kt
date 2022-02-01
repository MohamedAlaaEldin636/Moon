package app.grand.tafwak.presentation.auth.log_in

import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.account.use_case.UserLocalUseCase
import app.grand.tafwak.domain.auth.entity.model.User
import app.grand.tafwak.domain.auth.entity.request.LogInRequest
import app.grand.tafwak.domain.auth.use_case.LogInUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
  private val logInUseCase: LogInUseCase,
  val userLocalUseCase: UserLocalUseCase
) : BaseViewModel() {

  var request = LogInRequest()
  private val _logInResponse = MutableStateFlow<Resource<BaseResponse<User>>>(Resource.Default)
  val logInResponse = _logInResponse
  var registerStep = ""
  var validationException = SingleLiveEvent<Int>()

  init {
    viewModelScope.launch {
      userLocalUseCase.getRegisterStep().collect {
        registerStep = it
        if (registerStep.isNotEmpty())
          clickEvent.value = Constants.CONTINUE_PROGRESS
      }
    }


  }

  fun onLogInClicked() {
    logInUseCase(request)
      .catch { exception -> validationException.value = exception.message?.toInt() }
      .onEach { result ->
        _logInResponse.value = result
      }
      .launchIn(viewModelScope)
  }

}