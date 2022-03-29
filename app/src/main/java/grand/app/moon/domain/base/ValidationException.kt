package grand.app.moon.domain.base

import androidx.annotation.Keep

@Keep
open class ValidationException(
  @Transient
  var fieldsValidation: FieldsValidation = FieldsValidation.NO_ERROR,
  @Transient
  var errorMessage: String = "") {
}