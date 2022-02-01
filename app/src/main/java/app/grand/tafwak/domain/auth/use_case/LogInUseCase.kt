package app.grand.tafwak.domain.auth.use_case

import app.grand.tafwak.domain.account.use_case.UserLocalUseCase
import app.grand.tafwak.domain.auth.entity.model.User
import app.grand.tafwak.domain.auth.entity.request.LogInRequest
import app.grand.tafwak.domain.auth.entity.request.LogInValidationException
import app.grand.tafwak.domain.auth.enums.AuthFieldsValidation
import app.grand.tafwak.domain.auth.repository.AuthRepository
import app.grand.tafwak.domain.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class LogInUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userLocalUseCase: UserLocalUseCase
) {

  @Throws(LogInValidationException::class)
  operator fun invoke(request: LogInRequest): Flow<Resource<BaseResponse<User>>> = flow {
    if (request.email.isEmpty()) {
      throw LogInValidationException(AuthFieldsValidation.EMPTY_EMAIL.value.toString())
    }

    if (!request.email.isValidEmail()) {
      throw LogInValidationException(AuthFieldsValidation.INVALID_EMAIL.value.toString())
    }

    if (request.password.isEmpty()) {
      throw LogInValidationException(AuthFieldsValidation.EMPTY_PASSWORD.value.toString())
    }

    emit(Resource.Loading)
    val result = authRepository.logIn(request)
    if (result is Resource.Success) {
      userLocalUseCase.saveUserToken(result.value.data.token)
      userLocalUseCase.saveCountryId(result.value.data.country_id)
      if (result.value.data.register_steps == 4)
        userLocalUseCase.invoke(result.value.data)
      else
        userLocalUseCase.registerStep(
          result.value.data.register_steps.toString()
        )
    }
    emit(result)
  }.flowOn(Dispatchers.IO)
}