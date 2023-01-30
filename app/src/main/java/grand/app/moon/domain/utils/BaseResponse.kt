package grand.app.moon.domain.utils

import java.io.Serializable

data class BaseResponse<T>(
  var data: T,
  val message: String,
  val code: Int,
) : Serializable

fun <T, R> BaseResponse<T>.map(transformation: (T) -> R) = BaseResponse(transformation(data), message, code)
