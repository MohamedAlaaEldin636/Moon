package com.structure.base_mvvm.presentation.intro.tutorial

import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.general.use_case.GeneralUseCases
import com.structure.base_mvvm.domain.home.models.HomePaginateData
import com.structure.base_mvvm.domain.intro.entity.AppTutorial
import com.structure.base_mvvm.domain.intro.use_case.IntroUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

  fun setFirstTime(isFirstTime: Boolean) = generalUseCases.setFirstTimeUseCase(isFirstTime)

}