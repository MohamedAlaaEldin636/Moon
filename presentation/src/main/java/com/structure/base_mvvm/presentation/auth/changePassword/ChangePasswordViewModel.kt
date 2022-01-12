package com.structure.base_mvvm.presentation.auth.changePassword

import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.auth.entity.request.ChangePasswordRequest
import com.structure.base_mvvm.domain.auth.use_case.ChangePasswordUseCase
import com.structure.base_mvvm.domain.general.use_case.GeneralUseCases
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

  fun changePassword() {
    changePasswordUseCase(request)
      .catch { exception -> validationException.value = exception.message?.toInt() }
      .onEach { result ->
        _changePasswordResponse.value = result
      }
      .launchIn(viewModelScope)
  }
  fun isLogged() = generalUseCases.checkLoggedInUserUseCase()

}