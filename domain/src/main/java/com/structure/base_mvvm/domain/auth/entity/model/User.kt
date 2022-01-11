package com.structure.base_mvvm.domain.auth.entity.model

import androidx.annotation.Keep

@Keep
data class User(
  val email: String,
  val id: Int,
  val image: String,
  val register_steps: Int,
  val nickname: String?,
  val name: String,
  val phone: String,
  val token: String
)