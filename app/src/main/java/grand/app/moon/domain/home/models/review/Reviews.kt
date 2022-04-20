package grand.app.moon.domain.home.models.review

import androidx.annotation.Keep
import grand.app.moon.domain.auth.entity.model.User
import java.io.Serializable

@Keep
data class Reviews(
  var user: User = User(),
  var review: String = "",
  var date: String = "",
  var rate: String = "0"
): Serializable
