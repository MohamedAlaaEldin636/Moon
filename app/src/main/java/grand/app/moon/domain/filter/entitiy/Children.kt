package grand.app.moon.domain.filter.entitiy


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Children(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("is_range")
    val isRange: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("type")
    val type: Int = 0,
    var active: Int = 0,
    ) : Serializable