package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CategoryAdvertisement(
  @SerializedName("advertisements")
  val advertisements: ArrayList<Advertisement> = ArrayList(),
  @SerializedName("id")
  var id: Int = -1,
  @SerializedName("name")
  var name: String = "",
  @SerializedName("image")
  val image: String = "",
  var showMore: CategoryShowMore = CategoryShowMore(),
)