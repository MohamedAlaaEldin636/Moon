package grand.app.moon.presentation.more

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.presentation.addStore.BrowserHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  var userLocalUseCase: UserLocalUseCase,
  private val loginUseCase: LogInUseCase,
  val accountRepository: AccountRepository,
) : BaseViewModel() {
  @Bindable
  val accountAdapter = MoreAdapter()
  var isLogin = userLocalUseCase.isLoggin()
  var isFirst = false
  val browserHelper = BrowserHelper();

  private val TAG = "SettingsViewModel"
  val lastUrlStorage = accountRepository.getKeyFromLocal(Constants.LAST_URL)
  val countryIso = accountRepository.getKeyFromLocal(Constants.COUNTRY_ISO)

  init {
    browserHelper.lastUrl = "https://souqmoon.com/store/dashboard"
//    Log.d(TAG, ": ${countryIso}")
//    if (!browserHelper.isUser(lastUrlStorage))
//      browserHelper.lastUrl = lastUrlStorage

  }

  val logout =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)

  @Bindable
  val moreAdapter = MoreAdapter()
  val lang = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

  fun logout() {
    loginUseCase.logout().onEach {
      logout.value = it
    }.launchIn(viewModelScope)
  }

  fun saveUrl(link: String) {
    browserHelper.lastUrl = link
    accountRepository.saveKeyToLocal(Constants.LAST_URL, link)
  }

  fun getUrl(): String {
    return browserHelper.lastUrl
  }

  fun backToUserApp(): Boolean {
    if (!browserHelper.isUser()) {
      accountRepository.saveKeyToLocal(Constants.LAST_URL, "")
      return true
    }
    return false
  }
}