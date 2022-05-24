package grand.app.moon.presentation.intro.tutorial

import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.general.use_case.GeneralUseCases
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.intro.use_case.IntroUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
  private val generalUseCases: GeneralUseCases,
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  private val _appTutorialResponse =
    MutableStateFlow<Resource<BaseResponse<List<AppTutorial>>>>(Resource.Default)
  val appTutorialResponse = _appTutorialResponse

  val openIntro = SingleLiveEvent<Void>()

  init {
    settingsUseCase.onBoard("1").onEach { result ->
      println(result.toString())
      _appTutorialResponse.value = result
    }
      .launchIn(viewModelScope)
  }

  fun setFirstTime(isFirstTime: Boolean) {
    viewModelScope.launch {
      generalUseCases.setFirstTimeUseCase(isFirstTime)
    }
  }

  fun next(){
    submitEvent.value = Constants.NEXT
  }

  fun skip(){
    submitEvent.value = Constants.SKIP
  }

  fun setData(data: List<AppTutorial>) {

  }
}