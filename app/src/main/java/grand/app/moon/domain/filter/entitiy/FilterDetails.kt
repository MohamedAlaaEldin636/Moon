package grand.app.moon.domain.filter.entitiy


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import grand.app.moon.domain.home.models.Country
import java.io.Serializable

@Keep
data class FilterDetails(
//  @SerializedName("cities")
//  val cities: List<Country> = listOf(),
  @SerializedName("properties")
  var filterProperties: ArrayList<FilterProperty> = arrayListOf()
) : Serializable