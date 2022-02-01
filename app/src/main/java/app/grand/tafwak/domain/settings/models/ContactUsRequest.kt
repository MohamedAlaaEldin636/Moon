package app.grand.tafwak.domain.settings.models

import androidx.annotation.Keep

@Keep
class ContactUsRequest(
  var type: String = "",
  var name: String = "",
  var email: String = "",
  var phone: String = "",
  var message: String = "",
)
@Keep
class ContactAppValidationException(private val validationType: String) : Exception(validationType)