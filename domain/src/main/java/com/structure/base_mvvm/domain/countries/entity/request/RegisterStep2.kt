package com.structure.base_mvvm.domain.countries.entity.request

import androidx.annotation.Keep

@Keep
data class RegisterStep2(
  val register_steps: Int,
  val country_id: Int
)