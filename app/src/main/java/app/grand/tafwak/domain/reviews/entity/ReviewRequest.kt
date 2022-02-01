package app.grand.tafwak.domain.reviews.entity

import androidx.annotation.Keep

data class ReviewRequest(
  var rate: String = "",
  var review: String = "",
  var instructor_id: Int = 0,
)
@Keep
class ReviewValidationException(private val validationType: String) : Exception(validationType)