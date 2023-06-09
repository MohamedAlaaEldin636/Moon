package grand.app.moon.helpers.paging

import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.FailureStatus
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.orZero

fun <T> MAResult.Immediate<MABaseResponse<T>>.toResource(): Resource<BaseResponse<T?>> {
	return when (this) {
		is MAResult.Success -> Resource.Success(BaseResponse(value.data, value.message, value.code.orZero()))
		is MAResult.Failure -> Resource.Failure(
			failureStatus.toFailureStatus(),
			code.orZero(),
			message.orEmpty()
		)
	}
}

fun MAResult.Failure.Status.toFailureStatus(): FailureStatus {
	return when (this) {
		MAResult.Failure.Status.ERROR -> FailureStatus.OTHER
		MAResult.Failure.Status.TOKEN_EXPIRED -> FailureStatus.TOKEN_EXPIRED
		MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED -> FailureStatus.NOT_ACTIVE
		MAResult.Failure.Status.SERVER_ERROR -> FailureStatus.API_FAIL
		MAResult.Failure.Status.NO_INTERNET -> FailureStatus.NO_INTERNET
		MAResult.Failure.Status.OTHER -> FailureStatus.OTHER
	}
}

/**
 * - a [MAResult] can be [Loading], [Success] OR [Failure], However an [Immediate] result can only be
 * either [Success] OR [Failure], that's why I created this interface isa.
 */
sealed interface MAResult<T> {

    sealed interface Immediate<T> : MAResult<T>

    class Loading<T> : MAResult<T> {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Loading<*>) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    data class Success<T>(val value: T) : MAResult<T>, Immediate<T>

    data class Failure<T>(
      val failureStatus: Status,
      val code: Int? = null,
      val message: String? = null
    ) : MAResult<T>, Immediate<T> {

        fun getMessageNotEmptyNorNullOr(fallback: String): String = if (!message.isNullOrEmpty()) message else fallback

        enum class Status {
            /** 401 */
            ERROR,
            /** 403 */
            TOKEN_EXPIRED,
            /** 405 */
            ACTIVATION_NOT_VERIFIED,

            /** 500 until 600 */
            SERVER_ERROR,

            NO_INTERNET,

            OTHER
        }

    }

}

fun <T, R> MAResult<T>.map(successTransformation: (T) -> R): MAResult<R> {
    return when (this) {
        is MAResult.Failure -> MAResult.Failure(failureStatus, code, message)
        is MAResult.Success -> MAResult.Success(successTransformation(value))
        is MAResult.Loading -> MAResult.Loading()
    }
}

fun <T, R> MAResult.Immediate<T>.mapImmediate(successTransformation: (T) -> R): MAResult.Immediate<R> {
    return when (this) {
        is MAResult.Failure -> MAResult.Failure(failureStatus, code, message)
        is MAResult.Success -> MAResult.Success(successTransformation(value))
    }
}

fun <T> MAResult<T>.toSuccessOrNull(): MAResult.Success<T>? = this as? MAResult.Success<T>
