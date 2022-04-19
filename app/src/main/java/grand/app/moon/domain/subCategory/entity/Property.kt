package grand.app.moon.domain.subCategory.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Property(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("name")
    val name: String = "",
) : Serializable