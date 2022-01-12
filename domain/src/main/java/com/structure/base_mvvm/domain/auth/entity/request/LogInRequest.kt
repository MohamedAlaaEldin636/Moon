package com.structure.base_mvvm.domain.auth.entity.request

import androidx.annotation.Keep

@Keep
data class LogInRequest(
  var email: String = "",
  var password: String = "",
  var device_token: String = ""
)

@Keep
class LogInValidationException(private val validationType: String) : Exception(validationType)