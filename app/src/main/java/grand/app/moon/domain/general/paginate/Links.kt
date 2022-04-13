package com.structure.base_mvvm.domain.general.paginate

import java.io.Serializable

data class Links(
  val next: String ? ="",
  val last: String="",
  val prev: String="",
  val first: String="",
): Serializable