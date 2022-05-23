package grand.app.moon.domain.home.models

import androidx.annotation.Keep
import androidx.databinding.ObservableField
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class InteractionRequest(
  @SerializedName("advertisement_id")
  var advertisement_id: String? = null,
  @SerializedName("type")
  var type: Int? = null,
  @SerializedName("store_id")
  @Expose
  var store_id: Int? = null,
)

//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)