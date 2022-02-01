package app.grand.tafwak.presentation.teachers.more

import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.account.use_case.AccountUseCases
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(private val accountUseCases: AccountUseCases) : BaseViewModel() {

  private val _logOuResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val logOutResponse = _logOuResponse

  val showLogOutPopUp = SingleLiveEvent<Void>()

  fun logOut() {
    accountUseCases.logOutUseCase()
      .onEach { result ->
        _logOuResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun onLogOutClicked() {
    showLogOutPopUp.call()
  }
}