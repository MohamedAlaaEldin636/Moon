package com.structure.base_mvvm.presentation.base

import androidx.lifecycle.ViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent

open class BaseViewModel : ViewModel() {

  var dataLoadingEvent: SingleLiveEvent<Int> = SingleLiveEvent()
  var clickEvent: SingleLiveEvent<Int> = SingleLiveEvent()
  fun clickEvent(action: Int) {
    clickEvent.value = action
    clickEvent.call()
  }

}