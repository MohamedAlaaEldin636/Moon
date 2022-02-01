package app.grand.tafwak.domain.reviews.entity

import androidx.annotation.Keep
import app.grand.tafwak.domain.home.models.Instructor

@Keep
data class Reviews(
  var user: Instructor = Instructor(),
  var rate: String = "0",
  var review: String = ""
)
