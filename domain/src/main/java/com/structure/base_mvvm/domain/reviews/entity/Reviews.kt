package com.structure.base_mvvm.domain.reviews.entity

import androidx.annotation.Keep
import com.structure.base_mvvm.domain.home.models.Instructor

@Keep
data class Reviews(
  var user: Instructor = Instructor(),
  var rate: String = "0",
  var review: String = ""
)
