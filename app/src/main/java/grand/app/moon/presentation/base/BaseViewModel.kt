package grand.app.moon.presentation.base

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import es.dmoral.toasty.Toasty
import grand.app.moon.R

open class BaseViewModel : ViewModel(), Observable {
  private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

  var percentageAds = 90
  var clickEvent: SingleLiveEvent<Int> = SingleLiveEvent()
  var submitEvent: SingleLiveEvent<String> = SingleLiveEvent()
  fun clickEvent(action: Int) {
    clickEvent.value = action
  }

  fun submitEvent(action: String) {
    submitEvent.value = action
  }

  fun showError(context: Context, message : String){
    Toasty.error(context,message, Toast.LENGTH_SHORT, true).show();

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