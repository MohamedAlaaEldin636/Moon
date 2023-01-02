package grand.app.moon.domain.utils

sealed class Resource<out T> {

  fun <R> map(conversion: (T) -> R): Resource<R> {
    return when (this) {
      Default -> Default
      is Failure -> Failure(failureStatus, code, message)
      HideLoading -> HideLoading
      Loading -> Loading
      DONE -> DONE
      is Success -> Success(conversion(value))
    }
  }

  inline fun <R> mapSuccess(conversion: (T) -> R): Resource<R> {
    return when (this) {
      Default -> Default
      is Failure -> Failure(failureStatus, code, message)
      HideLoading -> HideLoading
      Loading -> Loading
      DONE -> DONE
      is Success -> Success(conversion(value))
    }
  }

  class Success<out T>(val value: T) : Resource<T>()

  class Failure(
    val failureStatus: FailureStatus,
    val code: Int? = null,
    val message: String? = null
  ) : Resource<Nothing>()

  object Loading : Resource<Nothing>()

  object DONE : Resource<Nothing>()

  object HideLoading : Resource<Nothing>()

  object Default : Resource<Nothing>()
}