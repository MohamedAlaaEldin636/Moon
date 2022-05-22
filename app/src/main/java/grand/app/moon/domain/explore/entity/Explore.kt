package grand.app.moon.domain.explore.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import grand.app.moon.domain.home.models.Store
import java.io.Serializable

@Keep
data class Explore(
  var id: Int = 0,
  @SerializedName("created_at")
  val createdAt: String = "",
  @SerializedName("file")
  var file: String = "",
  @SerializedName("mime_type")
  val mimeType: String = "",
  @SerializedName("likes_count")
  var likes: Int = 0,
  @SerializedName("shares_count")
  var shares: Int = 0,
  @SerializedName("comments_count")
  var comments: Int = 0,
  @SerializedName("is_liked")
  var isLike: Boolean = false,
  @SerializedName("store")
  var store: Store = Store(),


  ) : Serializable