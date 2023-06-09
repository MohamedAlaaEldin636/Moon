package grand.app.moon.domain.settings.models

import androidx.annotation.Keep

@Keep
data class SettingsData(
  val id: Int = -1,
  val title: String = "",
  val content: String = "",
  val image: String = ""
)