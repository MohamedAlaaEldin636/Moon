package com.structure.base_mvvm.presentation.intro.tutorial

import com.structure.base_mvvm.domain.general.use_case.GeneralUseCases
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(private val generalUseCases: GeneralUseCases) : BaseViewModel() {

  val openIntro = SingleLiveEvent<Void>()

  fun onSkipClicked() {
    openIntro.call()
  }
  fun setFirstTime(isFirstTime: Boolean) = generalUseCases.setFirstTimeUseCase(isFirstTime)
}