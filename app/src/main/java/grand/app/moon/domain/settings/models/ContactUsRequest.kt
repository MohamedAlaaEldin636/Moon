package grand.app.moon.domain.settings.models

import androidx.annotation.Keep
import androidx.databinding.ObservableField
import grand.app.moon.domain.base.ValidationException

@Keep
class ContactUsRequest(
  var type: String = "",
  var name: String = "",
  var email: String = "",
  var message: String = "",
  var reason_id: Int = -1,
  @Transient
  var contact: ObservableField<String> = ObservableField()
) : ValidationException()
@Keep
class ContactAppValidationException(private val validationType: String) : Exception(validationType)