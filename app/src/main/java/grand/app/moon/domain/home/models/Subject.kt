package grand.app.moon.domain.home.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Subject(
  @SerializedName("id")
  @Expose
  val id: Int = 0,
  @SerializedName("name")
  @Expose
  val name: String = "",
  @SerializedName("image")
  @Expose
  val image: String = "",
  )
