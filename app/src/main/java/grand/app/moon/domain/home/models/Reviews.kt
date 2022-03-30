package grand.app.moon.domain.home.models

import androidx.annotation.Keep
import grand.app.moon.domain.auth.entity.model.User

@Keep
data class Reviews(
  var user: User = User(),
  var review: String = "",
  var date: String = "",
  var rate: String = "0"
)
