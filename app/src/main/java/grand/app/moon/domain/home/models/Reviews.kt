package grand.app.moon.domain.home.models

import androidx.annotation.Keep

@Keep
data class Reviews(
  var id: Int = -1,
  var name: String = "",
  var content: String = "",
  var image: String = "",
  var rate: String = "0"
)
