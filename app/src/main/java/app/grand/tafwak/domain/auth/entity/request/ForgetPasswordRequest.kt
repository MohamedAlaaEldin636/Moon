package app.grand.tafwak.domain.auth.entity.request

import androidx.annotation.Keep

@Keep
data class ForgetPasswordRequest(
  var email: String = "",
  var type: String = ""
)