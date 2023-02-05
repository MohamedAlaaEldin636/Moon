package grand.app.moon.domain.utils

import grand.app.moon.helpers.paging.MABaseResponse
import java.io.Serializable

data class BaseResponse<T>(
  var data: T,
  val message: String,
  val code: Int,
) : Serializable

fun <T, R> BaseResponse<T>.map(transformation: (T) -> R) = BaseResponse(transformation(data), message, code)

fun <T> BaseResponse<T?>.toMABaseResponse(): MABaseResponse<T> {
	return MABaseResponse(data, message, code)
}
