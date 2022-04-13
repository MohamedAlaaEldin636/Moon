package grand.app.moon.domain.intro.entity

import java.io.Serializable


data class AppTutorial(
  var id: Int? = 0,
  var title: String? = "",
  var content: String? = "",
  var image: String = "",
  var active: Int? = 0
) : Serializable