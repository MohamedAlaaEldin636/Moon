package app.grand.tafwak.presentation.intro.intro

import app.grand.tafwak.domain.general.use_case.GeneralUseCases
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val generalUseCases: GeneralUseCases) : BaseViewModel() {

  val openLogIn = SingleLiveEvent<Void>()

  fun onLogInClicked() {
    openLogIn.call()
  }

  fun setFirstTime(isFirstTime: Boolean) = false
}