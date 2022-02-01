package app.grand.tafwak.domain.countries.entity.request

import androidx.annotation.Keep

@Keep
data class RegisterStep2(
  val register_steps: Int,
  val country_id: Int
)