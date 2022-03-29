package grand.app.moon.presentation.auth.log_in

import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
  private val logInUseCase: LogInUseCase,
  val userLocalUseCase: UserLocalUseCase
) : BaseViewModel() {

  var request = LogInRequest()
  var _logInResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val logInResponse = _logInResponse

  init {

  }


  private  val TAG = "LogInViewModel"
  fun onLogInClicked(v: View) {
    Log.d(TAG, "onLogInClicked: login")
    if (request.phone.trim().isEmpty()) {
      showError(v.context, v.context.getString(R.string.please_enter_your_phone));
      return
    }
    logInUseCase(request)
      .onEach { result ->
        _logInResponse.value = result
      }
      .launchIn(viewModelScope)
  }

}