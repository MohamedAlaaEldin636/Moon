package grand.app.moon.domain.home.models.review

import androidx.annotation.Keep
import androidx.databinding.ObservableField
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class ReviewRequest(
  @SerializedName("advertisement_id")
  @Expose
  var advertisement_id: String?,
  @SerializedName("store_id")
  @Expose
  var store_id: String?,
  @SerializedName("rate")
  @Expose
  var rate: String = "",
  @SerializedName("review")
  @Expose
  var review: String = ""
)

//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)