package grand.app.moon.domain.home.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
data class Instructor(
  @SerializedName("account_type")
  @Expose
  val accountType: String = "",
  @SerializedName("email")
  @Expose
  val email: String = "",
  @SerializedName("id")
  @Expose
  val id: Int = 0,
  @SerializedName("image")
  @Expose
  var image: String = "",
  @SerializedName("name")
  @Expose
  val name: String = "",
  @SerializedName("nickname")
  @Expose
  val nickname: String = "",
  @SerializedName("phone")
  @Expose
  val phone: String = "",
  @SerializedName("register_steps")
  @Expose
  val registerSteps: Int = 0,
  @SerializedName("token")
  @Expose
  val token: String = "",
  @SerializedName("average_rating")
  @Expose
  val average_rating: String = "0",
  @SerializedName("description")
  @Expose
  val description: String = "",
  @SerializedName("subject")
  @Expose
  val subject: Subject = Subject(),
  @SerializedName("classes")
  @Expose
  val classes: List<Classes> = listOf(),
<<<<<<< HEAD


  )
=======
  @SerializedName("reviews")
  @Expose
  val reviews: List<Reviews> = listOf(),
  ){
}
>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a
