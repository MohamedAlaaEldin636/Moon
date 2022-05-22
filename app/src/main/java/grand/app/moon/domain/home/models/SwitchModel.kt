package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class SwitchModel(
    @SerializedName("id")
    @Expose val id: Int = 0,
    @SerializedName("image")
    @Expose val image: String = "",
    @SerializedName("is_range")
    @Expose val isRange: Int = 0,
    @SerializedName("max")
    @Expose val max: Int = 0,
    @SerializedName("min")
    @Expose val min: Int = 0,
    @SerializedName("name")
    @Expose val name: String = "",
    @SerializedName("text")
    @Expose val text: String = "",
    @SerializedName("type")
    @Expose val type: Int = 0
)