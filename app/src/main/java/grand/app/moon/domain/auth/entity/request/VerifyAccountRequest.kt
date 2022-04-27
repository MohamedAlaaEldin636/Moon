package grand.app.moon.domain.auth.entity.request

import androidx.annotation.Keep

@Keep
data class VerifyAccountRequest(
  var type: String = "",
  var country_code: String = "",
  var phone: String = "",
  var code: String = "",
  @Transient
  var verificationId: String = "",
  )
