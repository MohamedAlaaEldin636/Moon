package grand.app.moon.domain.countries.entity


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
data class Country(
  @SerializedName("currency")
  @Expose
  val currency: String = "",
  @SerializedName("image")
  @Expose
  val image: String = "",
  @SerializedName("name")
  @Expose
  val name: String = "",
  @SerializedName("id")
  @Expose
  val id: Int = 0,
  @SerializedName("active")
  @Expose
  var active: Int = 0,
  @SerializedName("lang")
  @Expose
  val lang: String = "",
  @SerializedName("cities")
  @Expose
  val cities : List<Country> = arrayListOf(),
)