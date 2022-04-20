package grand.app.moon.domain.home.models.review

import androidx.annotation.Keep
import androidx.databinding.ObservableField

@Keep
class ReviewRequest(
  var advertisement_id: Int = 0,
  var rate: String = "",
  var review: String = ""
)

//@Keep
//class LogInValidationException(private val validationType: String) : Exception(validationType)