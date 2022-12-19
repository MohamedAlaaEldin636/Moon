package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ResponseAppGlobalAnnouncement(
  @SerializedName("category-ads")
    val categoryAds: ArrayList<CategoryAdvertisement> = arrayListOf(),
  @SerializedName("followings-stores")
    val followingsStores: List<Store> = listOf(),
  @SerializedName("most-popular-ads")
    val mostPopularAds: List<Advertisement> = listOf(),//1
  @SerializedName("most-rated-stores")
    val mostRatedStores: List<Store> = arrayListOf(),
  @SerializedName("suggested-ads")
    val suggestions: List<Advertisement> = listOf()//2
)