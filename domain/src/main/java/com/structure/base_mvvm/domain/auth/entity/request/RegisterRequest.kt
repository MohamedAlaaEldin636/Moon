package com.structure.base_mvvm.domain.auth.entity.request

import com.structure.base_mvvm.domain.utils.BaseRequest
import com.structure.base_mvvm.domain.utils.Constants

data class RegisterRequest(
  var name: String = "",
  var account_type: String = Constants.STUDENT_TYPE,
  var register_steps: Int = 1,
  var email: String = "",
  var password: String = "",
  var password_confirmation: String = "",
  var phone: String = "",
  var device_token: String = "",
  var isAccept: Boolean = false
) : BaseRequest()

class RegisterValidationException(private val validationType: String) : Exception(validationType)