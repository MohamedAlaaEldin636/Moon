package app.grand.tafwak.domain.reviews.entity

import com.google.gson.annotations.SerializedName
import app.grand.tafwak.domain.general.paginate.Paginate

class ReviewsMain(
  @SerializedName("data")
  val reviews: List<Reviews> = listOf()
) : Paginate()