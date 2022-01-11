package com.structure.base_mvvm.domain.auth.entity.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class LogInRequest(
  @SerializedName("phone_email")
  var email: String,
  var password: String,
  var device_token: String
) {
  constructor() : this("", "", "")

}
@Keep
class LogInValidationException(private val validationType: String) : Exception(validationType)