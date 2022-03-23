package grand.app.moon.domain.auth.entity.request

import androidx.annotation.Keep
import androidx.databinding.ObservableField

@Keep
class LogInRequest{
  var phone: String = ""
    set(value) {
      validation.phoneError.set(null)
      field = value
    }
  var device_token: String = ""
  var validation: LogInValidationException = LogInValidationException()
}

@Keep
class LogInValidationException {
  var phoneError: ObservableField<String> = ObservableField<String>()

}
//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)