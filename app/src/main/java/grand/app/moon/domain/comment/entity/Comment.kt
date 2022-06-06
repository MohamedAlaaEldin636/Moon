package grand.app.moon.domain.comment.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import grand.app.moon.domain.auth.entity.model.User

@Keep
data class Comment(
  @SerializedName("id")
  @Expose val id: Int = -1,
    @SerializedName("comment")
    @Expose val comment: String = "",
  @SerializedName("created_at")
  @Expose val createdAt: String = "",
    @SerializedName("user")
    @Expose val user: User? = User()
) {

}