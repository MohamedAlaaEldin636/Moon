package com.structure.base_mvvm.domain.educational.entity.request

import androidx.annotation.Keep

@Keep
data class RegisterStep3(
  val register_steps: Int = 3,
  val educational_stage_id: Int
)