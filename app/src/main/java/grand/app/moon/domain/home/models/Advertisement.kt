package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import grand.app.moon.domain.home.models.review.Reviews
import java.io.Serializable

@Keep
data class Advertisement(
  @SerializedName("average_rate")
  var averageRate: String = "0",
  @SerializedName("share_link")
  var share: String = "",
  @SerializedName("city")
  val city: Country = Country(),
  @SerializedName("country")
  val country: Country = Country(),
  @SerializedName("created_at")
  val createdAt: String = "",
  @SerializedName("description")
  val description: String = "",
  @SerializedName("favorite_count")
  var favoriteCount: Int = 0,
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("image")
  val image: String = "",
  @SerializedName("images2")
  var images: ArrayList<String> = arrayListOf(),
  @SerializedName("properties")
  val properties: ArrayList<Property> = arrayListOf(),
  @SerializedName("is_negotiable")
  val isNegotiable: Int = 0,
  @SerializedName("premium")
  val premium: Int = 0,
  @SerializedName("date")
  val date: Double = 0.0,
  @SerializedName("price")
  val price: Double = 0.0,
  @SerializedName("phone")
  val phone: String = "",
  @SerializedName("store2")
  val store: Store = Store(),
  @SerializedName("latitude")
  val latitude: Double = 0.0,
  @SerializedName("longitude")
  val longitude: Double = 0.0,
  @SerializedName("category_id")
  val categoryId: Int = 0,
  @SerializedName("sub_category_id")
  val subCategoryId: Int = 0,
  @SerializedName("title")
  val title: String = "",
  @SerializedName("views_count")
  var viewsCount: Int = 0,
  @SerializedName("share_count")
  var shareCount: Int = 0,
  @SerializedName("is_favorite")
  var isFavorite: Boolean = false,
  @SerializedName("switches")
  var switches: ArrayList<SwitchModel> = arrayListOf(),

  @SerializedName("reviews")
  var reviews: ArrayList<Reviews> = arrayListOf(),
  @SerializedName("similar_ads")
  var similarAds: ArrayList<Advertisement> = arrayListOf(),

  @SerializedName("store") var user: UserInAdvertisementOld? = null,

  @SerializedName("user_id") var userId: Int? = null,
  @SerializedName("store_id") var storeId: Int? = null,
  @SerializedName("shop_id") var shopId: Int? = null,
) : Serializable

data class UserInAdvertisementOld(
	var id: Int?,
)