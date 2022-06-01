package grand.app.moon.presentation.myAccount

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.more.MoreAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
  var userLocalUseCase: UserLocalUseCase,
  private val loginUseCase: LogInUseCase
) : BaseViewModel() {
  val logout =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)

  @Bindable
  val moreAdapter = MoreAdapter()
  var isLogin = userLocalUseCase.isLoggin()
  init {
  }

  fun logout(){
    loginUseCase.logout().onEach {
      logout.value = it
    }.launchIn(viewModelScope)
  }
}