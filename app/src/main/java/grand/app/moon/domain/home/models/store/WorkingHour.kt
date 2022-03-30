package grand.app.moon.domain.home.models.store


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class WorkingHour(
    @SerializedName("from")
    val from: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("to")
    val to: String = ""
)