package com.structure.base_mvvm.domain.auth.use_case

import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.LogInRequest
import com.structure.base_mvvm.domain.auth.entity.request.LogInValidationException
import com.structure.base_mvvm.domain.auth.entity.request.RegisterRequest
import com.structure.base_mvvm.domain.auth.entity.request.RegisterValidationException
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.domain.utils.isValidEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class RegisterUseCase @Inject constructor(
  private val authRepository: AuthRepository
) {

  @Throws(RegisterValidationException::class)
  operator fun invoke(request: RegisterRequest): Flow<Resource<BaseResponse<*>>> = flow {
    if (request.name.isEmpty()) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_NAME.value.toString())
    }
    if (request.name.isEmpty()) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_NAME.value.toString())
    }

    if (request.email.isEmpty()) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_EMAIL.value.toString())
    }

    if (!request.email.isValidEmail()) {
      throw RegisterValidationException(AuthFieldsValidation.INVALID_EMAIL.value.toString())
    }

    if (request.password.isEmpty()) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_PASSWORD.value.toString())
    }
    if (request.password_confirmation.isEmpty()) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_CONFIRM_PASSWORD.value.toString())
    }
    if (request.password_confirmation != request.password) {
      throw RegisterValidationException(AuthFieldsValidation.PASSWORD_NOT_MATCH.value.toString())
    }

    if (!request.isAccept) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_TERMS.value.toString())
    }
    if (request.image.isEmpty()) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_IMAGE.value.toString())
    }

    emit(Resource.Loading)
    val result = authRepository.register(request)
    emit(result)
  }.flowOn(Dispatchers.IO)
}