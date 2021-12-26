package com.structure.base_mvvm.presentation.profile.viewModels

import android.widget.CompoundButton
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
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