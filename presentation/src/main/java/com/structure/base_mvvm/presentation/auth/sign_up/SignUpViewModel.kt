package com.structure.base_mvvm.presentation.auth.sign_up

import android.widget.CompoundButton
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.RegisterRequest
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.auth.use_case.RegisterUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
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