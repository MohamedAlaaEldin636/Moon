package grand.app.moon.helpers.paging

data class MABaseResponse<T>(
    val data: T?,
    val message: String,
    val code: Int,
)

fun <T, R> MABaseResponse<T>.map(transformation: (T?) -> R?) = MABaseResponse(transformation(data), message, code)
