package com.structure.base_mvvm.domain.auth.use_case

import com.structure.base_mvvm.domain.account.use_case.UserLocalUseCase
import com.structure.base_mvvm.domain.auth.entity.request.RegisterRequest
import com.structure.base_mvvm.domain.auth.entity.request.RegisterValidationException
import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.domain.utils.isValidEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class RegisterUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userLocalUseCase: UserLocalUseCase
) {

  @Throws(RegisterValidationException::class)
  operator fun invoke(request: RegisterRequest): Flow<Resource<BaseResponse<*>>> = flow {
    if (request.name.isEmpty()) {
      throw RegisterValidationException(AuthFieldsValidation.EMPTY_NAME.value.toString())
    }
    if (request.account_type == Constants.TEACHER_TYPE)
      if (request.nickname.isEmpty()) {
        throw RegisterValidationException(AuthFieldsValidation.EMPTY_NICK_NAME.value.toString())
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
    if (result is Resource.Success) {
      userLocalUseCase.registerStep((request.register_steps + 1).toString())
    }
    emit(result)
  }.flowOn(Dispatchers.IO)
}