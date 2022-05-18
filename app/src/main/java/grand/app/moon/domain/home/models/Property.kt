package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import retrofit2.http.Query
import java.io.Serializable

@Keep
data class Property(
  @Query("id")
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("image")
  val image: String = "",
  @SerializedName("name")
  val name: String = "",
  @SerializedName("parent")
  val parent: Parent = Parent(),
  @SerializedName("text")
  val text: String = "",
) : Serializable