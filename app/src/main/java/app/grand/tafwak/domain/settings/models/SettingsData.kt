package app.grand.tafwak.domain.settings.models

import androidx.annotation.Keep

@Keep
data class SettingsData(
  val title: String = "",
  val content: String = "",
  val image: String = ""
)