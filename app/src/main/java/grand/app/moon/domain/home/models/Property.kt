package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Property(
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("image")
  val image: String = "",
  @SerializedName("name")
  val name: String = "",
  @SerializedName("parent")
  val parent: Parent = Parent()
)