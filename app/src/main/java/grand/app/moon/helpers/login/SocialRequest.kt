package grand.app.moon.helpers.login

import androidx.annotation.Keep
import androidx.databinding.ObservableField

@Keep
class SocialRequest(
  var name: String = "",
  var email: String = "",
  var objective: String = "google",
  var provider_id: String = "",
){

}

//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)