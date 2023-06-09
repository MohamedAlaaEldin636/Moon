package grand.app.moon.domain.story.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import grand.app.moon.domain.shop.StoryLink
import java.io.Serializable

@Keep
data class StoryItem(
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("created_at")
  val createdAt: String = "",
  @SerializedName("share_link")
  val shareLink: String = "",
  @SerializedName("file")
  val file: String = "",
  @SerializedName("mime_type")
  val mimeType: String = "",
  @SerializedName("updated_at")
  val updatedAt: String = "",
  @SerializedName("is_seen")
  var isSeen: Boolean = false,
  @SerializedName("is_liked")
  var is_liked: Boolean = false,
  @SerializedName("name")
  val name: String = "",
  var isFirst: Boolean = false,
  @SerializedName("story_link_type") var storyLinkType: Int? = StoryLink.CALL.apiValue,
) : Serializable