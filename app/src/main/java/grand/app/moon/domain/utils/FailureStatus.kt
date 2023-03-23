package grand.app.moon.domain.utils

import grand.app.moon.helpers.paging.MAResult

enum class FailureStatus {
  EMPTY,
  API_FAIL,
  NO_INTERNET,
  OTHER,
  NOT_ACTIVE,
  TOKEN_EXPIRED
}

fun MAResult.Failure<*>.toFailureStatus(): FailureStatus {
	return when (failureStatus) {
		MAResult.Failure.Status.ERROR -> FailureStatus.EMPTY
		MAResult.Failure.Status.TOKEN_EXPIRED -> FailureStatus.TOKEN_EXPIRED
		MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED -> FailureStatus.NOT_ACTIVE
		MAResult.Failure.Status.SERVER_ERROR -> FailureStatus.API_FAIL
		MAResult.Failure.Status.NO_INTERNET -> FailureStatus.NO_INTERNET
		MAResult.Failure.Status.OTHER -> FailureStatus.OTHER
	}
}
