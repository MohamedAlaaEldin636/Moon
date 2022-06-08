package grand.app.moon.domain.auth.entity.model

import androidx.annotation.Keep
import java.io.Serializable


@Keep
data class User(
  val id: Int = 0,
  val name: String?= "",
  val email: String = "",
  val image: String? = "",
  val phone: String = "",
  val token: String = "",
  val country_code: String = "",
) : Serializable