package grand.app.moon.presentation.more

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.use_case.LogInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  var userLocalUseCase: UserLocalUseCase,
  private val loginUseCase: LogInUseCase,
  val accountRepository: AccountRepository
  ) : BaseViewModel() {
  @Bindable
  val accountAdapter = MoreAdapter()
  var isLogin = userLocalUseCase.isLoggin()

  val logout =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)

  @Bindable
  val moreAdapter = MoreAdapter()
  val lang = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

  fun logout(){
    loginUseCase.logout().onEach {
      logout.value = it
    }.launchIn(viewModelScope)
  }
}