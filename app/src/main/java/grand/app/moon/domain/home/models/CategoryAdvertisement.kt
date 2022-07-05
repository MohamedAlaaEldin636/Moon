package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CategoryAdvertisement(
  @SerializedName("advertisements")
  var advertisements: ArrayList<Advertisement> = ArrayList(),
  @SerializedName("id")
  var id: Int = -1,
  @SerializedName("ads_count")
  var adsCount: Int = -1,

  @SerializedName("type")
  var type: Int = -1,
  @SerializedName("name")
  var name: String = "",
  @SerializedName("image")
  val image: String? = "",
  var showMore: CategoryShowMore = CategoryShowMore(),
)