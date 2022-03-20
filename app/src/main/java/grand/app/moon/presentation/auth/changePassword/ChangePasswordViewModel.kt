package grand.app.moon.presentation.auth.changePassword

import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.auth.entity.request.ChangePasswordRequest
import grand.app.moon.domain.auth.use_case.ChangePasswordUseCase
import grand.app.moon.domain.general.use_case.GeneralUseCases
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
  private val changePasswordUseCase: ChangePasswordUseCase,
  private val generalUseCases: GeneralUseCases
) :
  BaseViewModel() {
  var request = ChangePasswordRequest()
  private val _changePasswordResponse =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val changePasswordResponse = _changePasswordResponse
  var validationException = SingleLiveEvent<Int>()

  var isLogged: Boolean = false

  init {
    viewModelScope.launch {
      generalUseCases.checkLoggedInUserUseCase().collect {
//        isLogged = it
//        notifyPropertyChanged(BR.isLogged)
      }
    }
  }

  fun changePassword() {
    changePasswordUseCase(request)
      .catch { exception -> validationException.value = exception.message?.toInt() }
      .onEach { result ->
        _changePasswordResponse.value = result
      }
      .launchIn(viewModelScope)
  }


}