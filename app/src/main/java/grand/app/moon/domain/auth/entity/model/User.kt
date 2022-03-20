package grand.app.moon.domain.auth.entity.model

import androidx.annotation.Keep


@Keep
data class User(
  val email: String = "",
  val id: Int = 0,
  val image: String = "",
  val register_steps: Int = 0,
  val nickname: String? = "",
  val name: String = "",
  val phone: String = "",
  val account_type: String = "",
  val country_id: String = "",
  val token: String = ""
)