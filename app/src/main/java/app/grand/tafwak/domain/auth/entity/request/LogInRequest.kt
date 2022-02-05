package app.grand.tafwak.domain.auth.entity.request

import android.util.Log
import androidx.annotation.Keep
import androidx.databinding.ObservableField

@Keep
class LogInRequest {
  var email: String = ""
    set(value) {
      validation.emailError.set(null)
      Log.e("validation", ": " + validation.emailError.get())
      field = value
    }
  var password: String = ""
  var device_token: String = ""
  var validation: LogInValidationException = LogInValidationException()
}

@Keep
class LogInValidationException {
  var emailError: ObservableField<String> = ObservableField<String>()
  var passwordError: ObservableField<String> = ObservableField<String>()

}
//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)