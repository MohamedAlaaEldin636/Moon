package com.structure.base_mvvm.domain.auth.entity.request

import androidx.annotation.Keep

@Keep
data class ChangePasswordRequest(
  var password: String = "",
  var password_confirmation: String = "",

  )

@Keep
class ChangePasswordValidationException(private val validationType: String) :
  Exception(validationType)