package com.structure.base_mvvm.presentation.auth.log_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.account.use_case.UserLocalUseCase
import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.LogInRequest
import com.structure.base_mvvm.domain.auth.use_case.LogInUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
  private val logInUseCase: LogInUseCase,
  private val userLocalUseCase: UserLocalUseCase
) : BaseViewModel() {

  var request = LogInRequest()
  private val _logInResponse = MutableStateFlow<Resource<BaseResponse<User>>>(Resource.Default)
  val logInResponse = _logInResponse
  var regsiter_step = ""
  var validationException = SingleLiveEvent<Int>()

  init {
    regsiter_step = checkIfHasAccount()
    Log.e("regsiter_step", ": "+regsiter_step )
    if (regsiter_step.isNotEmpty())
      clickEvent.value = Constants.CONTINUE_PROGRESS

  }

  fun onLogInClicked() {
    logInUseCase.login(request)
      .catch { exception -> validationException.value = exception.message?.toInt() }
      .onEach { result ->
        _logInResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  private fun checkIfHasAccount(): String = userLocalUseCase.invoke(Constants.REGISTER_STEP)

}