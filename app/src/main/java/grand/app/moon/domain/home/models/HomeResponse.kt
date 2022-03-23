package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class HomeResponse(
  @SerializedName("category-ads")
    val categoryAds: List<CategoryAd> = listOf(),
  @SerializedName("followings-stores")
    val followingsStores: List<Any> = listOf(),
  @SerializedName("most-popular-ads")
    val mostPopularAds: List<MostPopularAd> = listOf(),
  @SerializedName("most-rated-stores")
    val mostRatedStores: List<Store> = arrayListOf(),
  @SerializedName("suggested-ads")
    val suggestedAds: List<SuggestedAd> = listOf()
)