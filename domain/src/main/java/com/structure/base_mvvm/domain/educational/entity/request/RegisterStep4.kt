package com.structure.base_mvvm.domain.educational.entity.request

import androidx.annotation.Keep

@Keep
data class RegisterStep4(
  val register_steps: Int=4,
  val grade_id: Int
)