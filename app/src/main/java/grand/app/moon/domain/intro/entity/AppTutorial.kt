package grand.app.moon.domain.intro.entity

import grand.app.moon.R
import java.io.Serializable


data class AppTutorial(
  var id: Int = 0,
  var name: String? = "",
  var title: String? = "",
  var content: String? = "",
  var image: String = "",
  var active: Int? = 0,
  var drawable: Int = R.drawable.dot_1
) : Serializable