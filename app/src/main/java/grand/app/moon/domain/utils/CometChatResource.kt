package grand.app.moon.domain.utils

sealed class CometChatResource<out T> {

  class Success<out T>(val value: T) : CometChatResource<T>()

  class Failure(
    val failureStatus: FailureStatus,
    val code: Int? = null,
    val message: String? = null
  ) : CometChatResource<Nothing>()

}