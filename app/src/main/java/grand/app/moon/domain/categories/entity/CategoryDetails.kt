package grand.app.moon.domain.categories.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store

@Keep
data class CategoryDetails(
    @SerializedName("ads_count")
    val adsCount: Int = 0,
    @SerializedName("stores")
    var stores: ArrayList<Store> = arrayListOf(),
    @SerializedName("advertisements")
    var advertisements: List<Advertisement> = listOf()
)