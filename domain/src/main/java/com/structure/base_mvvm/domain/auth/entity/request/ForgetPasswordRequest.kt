package com.structure.base_mvvm.domain.auth.entity.request

import androidx.annotation.Keep

@Keep
data class ForgetPasswordRequest(
  var email: String = "",
  var type: String = ""
)