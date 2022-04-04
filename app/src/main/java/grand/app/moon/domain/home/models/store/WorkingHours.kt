package grand.app.moon.domain.home.models.store


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class WorkingHours(
  @SerializedName("day")
  var day: String = "",
  @SerializedName("from")
  val from: String = "",
  @SerializedName("status")
  val status: Boolean = false,
  @SerializedName("to")
  val to: String = ""
): Serializable