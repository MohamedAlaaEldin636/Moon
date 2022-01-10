package com.structure.base_mvvm.presentation.base

import androidx.lifecycle.ViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
import java.util.*
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.SavedStateHandle

open class BaseViewModel : ViewModel(), Observable {
  private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

  var dataLoadingEvent: SingleLiveEvent<Int> = SingleLiveEvent()
  var clickEvent: SingleLiveEvent<Int> = SingleLiveEvent()
  fun clickEvent(action: Int) {
    clickEvent.value = action
    clickEvent.call()
  }

  override fun addOnPropertyChangedCallback(
    callback: Observable.OnPropertyChangedCallback
  ) {
    callbacks.add(callback)
  }

  override fun removeOnPropertyChangedCallback(
    callback: Observable.OnPropertyChangedCallback
  ) {
    callbacks.remove(callback)
  }

  /**
   * Notifies observers that all properties of this instance have changed.
   */
  fun notifyChange() {
    callbacks.notifyCallbacks(this, 0, null)
  }

  /**
   * Notifies observers that a specific property has changed. The getter for the
   * property that changes should be marked with the @Bindable annotation to
   * generate a field in the BR class to be used as the fieldId parameter.
   *
   * @param fieldId The generated BR id for the Bindable field.
   */
  fun notifyPropertyChanged(fieldId: Int) {
    callbacks.notifyCallbacks(this, fieldId, null)
  }

//  fun getArgs(key: String, dataType: Objects, savedStateHandle: SavedStateHandle): Any? {
//    savedStateHandle.get<dataType>(key)?.let {
//      return it
//    }
//    return null
//  }
}