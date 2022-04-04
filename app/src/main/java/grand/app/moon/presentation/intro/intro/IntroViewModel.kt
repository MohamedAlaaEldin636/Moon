package grand.app.moon.presentation.intro.intro

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.general.use_case.GeneralUseCases
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
  private val generalUseCases: GeneralUseCases,
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  val positions = ObservableField(0)
  val isLast = ObservableBoolean(false)
  val title = ObservableField("")
  val content = ObservableField("")
  val data = BaseResponse<List<AppTutorial>>(arrayListOf(), "", -1)
  private val _appTutorialResponse =
    MutableStateFlow<Resource<BaseResponse<List<AppTutorial>>>>(Resource.Default)
  val appTutorialResponse = _appTutorialResponse

  val openIntro = SingleLiveEvent<Void>()

  fun callService() {
    settingsUseCase.onBoard("1").onEach { result ->
      println(result.toString())
      _appTutorialResponse.value = result
    }
      .launchIn(viewModelScope)
  }

  fun setFirstTime() {
    accountRepository.saveKeyToLocal(Constants.INTRO,"true")
  }

  fun next() {
    submitEvent.value = Constants.NEXT
  }

  fun skip() {
    submitEvent.value = Constants.SKIP
  }

  private val TAG = "IntroViewModel"
  fun setData(data: List<AppTutorial>) {
    this.data.data = data
    Log.d(TAG, "setData: ${data.size}")
    Log.d(TAG, "setDataPosition: ${positions.get()}")
    if(positions.get()!! == -1) positions.set(0)
    if (positions.get()!! < data.size) {
      title.set(data[positions.get()!!].title)
      content.set(data[positions.get()!!].content)
      isLast.set(positions.get() == data.size - 1)
    }
    show.set(true)
  }
}