package grand.app.moon.domain.home.models

import androidx.annotation.Keep
import androidx.databinding.ObservableField
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class InteractionRequest(
  @SerializedName("advertisement_id")
  @Expose
  var advertisement_id: Int? = null,
  @SerializedName("type")
  @Expose
  var type: Int? = null,
)

//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)