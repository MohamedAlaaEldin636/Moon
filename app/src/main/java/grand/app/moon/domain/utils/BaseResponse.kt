package grand.app.moon.domain.utils

import java.io.Serializable

data class BaseResponse<T>(
  var data: T,
  val message: String,
  val code: Int,
) : Serializable