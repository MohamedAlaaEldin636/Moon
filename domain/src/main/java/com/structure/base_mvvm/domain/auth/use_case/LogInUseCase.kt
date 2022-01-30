package com.structure.base_mvvm.domain.auth.use_case

import com.structure.base_mvvm.domain.account.use_case.UserLocalUseCase
import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.LogInRequest
import com.structure.base_mvvm.domain.auth.entity.request.LogInValidationException
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.utils.*
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