package app.grand.tafwak.domain.home.models

import androidx.annotation.Keep

@Keep
data class HomeData(
  val created_at: String,
  val text: String,
  val id: Int,
  val type: String,
  val user_id: Int,
  val order_id: Int,
  val order_service_id: Int
)