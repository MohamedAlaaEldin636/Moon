package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Advertisement(
  @SerializedName("average_rate")
  val averageRate: String? = "0",
  @SerializedName("city")
  val city: Country = Country(),
  @SerializedName("country")
  val country: Country = Country(),
  @SerializedName("created_at")
  val createdAt: String = "",
  @SerializedName("description")
  val description: String = "",
  @SerializedName("favorite_count")
  val favoriteCount: Int = 0,
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("image")
  val image: String = "",
  @SerializedName("images")
  var images: ArrayList<String> = arrayListOf(),
  @SerializedName("properties")
  val properties: ArrayList<Property> = arrayListOf(),
  @SerializedName("is_negotiable")
  val isNegotiable: Int = 0,
  @SerializedName("premium")
  val premium: Int = 0,
  @SerializedName("price")
  val price: Int = 0,
  @SerializedName("store")
  val store: Store? = Store(),
  @SerializedName("latitude")
  val latitude: Double = 0.0,
  @SerializedName("longitude")
  val longitude: Double = 0.0,
  @SerializedName("category_id")
  val categoryId: Int = 0,
  @SerializedName("title")
  val title: String = "",
  @SerializedName("views_count")
  val viewsCount: Int = 0,
  @SerializedName("share_count")
  val shareCount: Int = 0,
  @SerializedName("is_favorite")
  var isFavorite: Boolean = false,

  @SerializedName("reviews")
  var reviews: ArrayList<Reviews> = arrayListOf(),


  ) : Serializable