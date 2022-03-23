package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class MostPopularAd(
    @SerializedName("average_rate")
    val averageRate: Int = 0,
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
    @SerializedName("images")
    val images: List<ImageModel> = listOf(),
    @SerializedName("is_negotiable")
    val isNegotiable: Int = 0,
    @SerializedName("premium")
    val premium: Int = 0,
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("store")
    val store: Store = Store(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("views_count")
    val viewsCount: Int = 0
)