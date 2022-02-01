package app.grand.tafwak.presentation.intro.tutorial

import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.general.use_case.GeneralUseCases
import app.grand.tafwak.domain.intro.entity.AppTutorial
import app.grand.tafwak.domain.intro.use_case.IntroUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
  private val generalUseCases: GeneralUseCases,
  private val introUseCase: IntroUseCase
) : BaseViewModel() {
  private val _appTutorialResponse =
    MutableStateFlow<Resource<BaseResponse<List<AppTutorial>>>>(Resource.Default)
  val appTutorialResponse = _appTutorialResponse

  val openIntro = SingleLiveEvent<Void>()

  init {
    introUseCase.invoke().onEach { result ->
      println(result.toString())
      _appTutorialResponse.value = result
    }
      .launchIn(viewModelScope)
  }

  fun onSkipClicked() {
    openIntro.call()
  }

  fun setFirstTime(isFirstTime: Boolean) {
    viewModelScope.launch {
      generalUseCases.setFirstTimeUseCase(isFirstTime)
    }
  }

}