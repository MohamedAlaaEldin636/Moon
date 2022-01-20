package com.structure.base_mvvm.domain.settings.models

import androidx.annotation.Keep

@Keep
class SettingsData(
  val title: String = "",
  val content: String = "",
  val image: String = ""
)