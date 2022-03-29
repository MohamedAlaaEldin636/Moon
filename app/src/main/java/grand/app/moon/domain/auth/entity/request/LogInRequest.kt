package grand.app.moon.domain.auth.entity.request

import androidx.annotation.Keep
import androidx.databinding.ObservableField

@Keep
class LogInRequest{
  var phone: String = ""
  var country_code: String = ""
}

//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)