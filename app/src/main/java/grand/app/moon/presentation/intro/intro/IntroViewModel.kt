package grand.app.moon.presentation.intro.intro

import grand.app.moon.domain.general.use_case.GeneralUseCases
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
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