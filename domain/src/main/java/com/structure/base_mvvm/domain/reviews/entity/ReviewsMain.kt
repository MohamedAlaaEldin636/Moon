package com.structure.base_mvvm.domain.reviews.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Paginate

class ReviewsMain(
  @SerializedName("data")
  val reviews: List<Reviews> = listOf()
) : Paginate()