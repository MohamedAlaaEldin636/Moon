package com.structure.base_mvvm.domain.auth.entity.request

data class ForgetPasswordRequest(
  var email: String = "",
  var type: String = ""
)