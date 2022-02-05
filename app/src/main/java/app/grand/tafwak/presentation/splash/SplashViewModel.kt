package app.grand.tafwak.presentation.splash

import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.general.use_case.GeneralUseCases
import app.grand.tafwak.presentation.base.utils.Constants
import app.grand.tafwak.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val generalUseCases: GeneralUseCases) :
  BaseViewModel() {
  init {
    viewModelScope.launch {
      delay(2000)
      generalUseCases.checkFirstTimeUseCase().collect { isFirst ->
        if (isFirst) {
          clickEvent.value = Constants.FIRST_TIME
        } else {
          generalUseCases.checkLoggedInUserUseCase().collect { user ->

            if (user.account_type.isNotEmpty())
              clickEvent.value = Constants.IS_LOGGED
            else
              clickEvent.value = Constants.AUTH
          }
        }
      }
    }
  }

}