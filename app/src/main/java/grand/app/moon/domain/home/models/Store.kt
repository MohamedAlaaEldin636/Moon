package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Store(
    @SerializedName("advertisements_count")
    val advertisementsCount: String = "0",
    @SerializedName("average_rate")
    var averageRate: String = "0",
    @SerializedName("email")
    val email: String = "",
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
    val token: Any = Any(),
    @SerializedName("views_count")
    val viewsCount: String = "0"
)