package grand.app.moon.domain.home.models.store


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class SocialLink(
    @SerializedName("link")
    val link: String = "",
    @SerializedName("type")
    val type: String = ""
)