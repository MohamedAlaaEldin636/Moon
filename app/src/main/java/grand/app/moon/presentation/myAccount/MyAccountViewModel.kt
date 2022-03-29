package grand.app.moon.presentation.myAccount

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.presentation.more.MoreAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
  var userLocalUseCase: UserLocalUseCase,
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  @Bindable
  val moreAdapter = MoreAdapter()
  var isLogin = false
  init {
    checkLogin()
  }

  fun checkLogin(){
    isLogin = userLocalUseCase.isLoggin()
  }
}