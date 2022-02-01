package app.grand.tafwak.presentation.profile.viewModels

import android.widget.CompoundButton
import app.grand.tafwak.domain.auth.repository.AuthRepository
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

  val backToPreviousScreen = SingleLiveEvent<Void>()

  fun onBackClicked() {
    backToPreviousScreen.call()
  }
  fun onCheckedChange(button: CompoundButton?, check: Boolean) {
//    if (check) liveData.setValue(Mutable(Constants.TERMS))
//    isTermsAccepted = check
  }
}