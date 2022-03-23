package grand.app.moon.domain.language


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
data class Language(
  @SerializedName("lang")
  @Expose
  val language: String = "",
  @SerializedName("name")
  @Expose
  val name: String = ""
)