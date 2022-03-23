package grand.app.moon.domain.story.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class StoryItem(
    @SerializedName("category_id")
    val categoryId: Int = 0,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("file")
    val `file`: String = "",
    @SerializedName("file_type")
    val fileType: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("updated_at")
    val updatedAt: Any = Any(),
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    val isFirst: Boolean = false
)