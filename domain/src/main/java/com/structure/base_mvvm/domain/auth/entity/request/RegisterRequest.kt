package com.structure.base_mvvm.domain.auth.entity.request

import androidx.annotation.Keep
import com.structure.base_mvvm.domain.utils.BaseRequest
import com.structure.base_mvvm.domain.utils.Constants
@Keep
data class RegisterRequest(
  var name: String = "",
  var nickname: String = "",
  var account_type: String = Constants.STUDENT_TYPE,
  var register_steps: Int = 1,
  var email: String = "",
  var password: String = "",
  var password_confirmation: String = "",
  var phone: String = "",
  var device_token: String = "",
  var isAccept: Boolean = false
) : BaseRequest()
@Keep
class RegisterValidationException(private val validationType: String) : Exception(validationType)