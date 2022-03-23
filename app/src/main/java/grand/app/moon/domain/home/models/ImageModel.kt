package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ImageModel(
    @SerializedName("advertisement_id")
    val advertisementId: Int = 0,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("updated_at")
    val updatedAt: Any = Any()
)