package grand.app.moon.domain.auth.use_case

import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.auth.enums.AuthFieldsValidation
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ChangePasswordUseCase @Inject constructor(
  private val authRepository: AuthRepository) {

  @Throws(ChangePasswordValidationException::class)
  operator fun invoke(request: ChangePasswordRequest): Flow<Resource<BaseResponse<*>>> = flow {
    if (request.password.isEmpty()) {
      throw ChangePasswordValidationException(AuthFieldsValidation.EMPTY_PASSWORD.value.toString())
    }

    if (request.password_confirmation.isEmpty()) {
      throw ChangePasswordValidationException(AuthFieldsValidation.EMPTY_CONFIRM_PASSWORD.value.toString())
    }

    if (request.password_confirmation != request.password) {
      throw RegisterValidationException(AuthFieldsValidation.PASSWORD_NOT_MATCH.value.toString())
    }
    emit(Resource.Loading)
    val result = authRepository.changePassword(request)
    emit(result)
  }.flowOn(Dispatchers.IO)
}