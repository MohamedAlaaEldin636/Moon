package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import grand.app.moon.domain.home.models.store.SocialLink
import grand.app.moon.domain.home.models.store.WorkingHours
import java.io.Serializable

@Keep
data class Store(
  @SerializedName("advertisements_count")
  val advertisementsCount: String = "0",
  @SerializedName("average_rate")
  var averageRate: String? = "0",
  @SerializedName("email")
  val email: String = "",
  @SerializedName("website")
  var website: String = "",
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("image")
  val image: String = "",
  @SerializedName("name")
  val name: String = "",
  @SerializedName("nickname")
  val nickname: String = "",
  @SerializedName("phone")
  val phone: String = "",
  @SerializedName("token")
  val token: String = "",
  @SerializedName("background_image")
  val backgroundImage: String = "",
  @SerializedName("views_count")
  val viewsCount: String = "0",
  @SerializedName("rate_count")
  val rateCount: String = "0",
  @SerializedName("followers_count")
  val followersCount: String = "0",
  @SerializedName("latitude")
  val latitude: Double = 0.0,
  @SerializedName("longitude")
  val longitude: Double = 0.0,



  @SerializedName("is_following")
  var isFollowing: Boolean = true,
  @SerializedName("social_media_links")
  var socialMediaLinks: ArrayList<SocialLink> = arrayListOf(),
  @SerializedName("working_hours")
  var workingHours: ArrayList<WorkingHours> = arrayListOf(),
  @SerializedName("advertisements")
  var advertisements: ArrayList<Advertisement> = arrayListOf(),
  @SerializedName("city")
  val city: Country = Country(),
  @SerializedName("country")
  val country: Country = Country(),
  @SerializedName("description")
  val description: String = "",
  @SerializedName("created_at")
  val createdAt: String = "",


  ) : Serializable