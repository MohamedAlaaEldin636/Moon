package app.grand.tafwak.domain.auth.entity.request

import androidx.annotation.Keep

@Keep
data class VerifyAccountRequest(
  var type: String = "",
  var register_steps: Int = 1,
  var email: String = "",
  var code: String = ""
)
