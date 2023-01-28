package grand.app.moon.helpers.paging

import grand.app.moon.domain.utils.BaseResponse

data class MABaseResponse<T>(
    val data: T?,
    val message: String,
    val code: Int,
)

fun <T, R> MABaseResponse<T>.map(transformation: (T?) -> R?) = MABaseResponse(transformation(data), message, code)

fun <T> MABaseResponse<T>.toBaseResponse(): BaseResponse<T?> {
	return BaseResponse(data, message, code)
}
