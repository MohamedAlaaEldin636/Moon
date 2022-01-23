package com.structure.base_mvvm.domain.settings.use_case

import com.structure.base_mvvm.domain.auth.enums.AuthFieldsValidation
import com.structure.base_mvvm.domain.settings.models.ContactAppValidationException
import com.structure.base_mvvm.domain.settings.models.ContactUsRequest
import com.structure.base_mvvm.domain.settings.models.SettingsData
import com.structure.base_mvvm.domain.settings.repository.SettingsRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.domain.utils.isValidEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class SettingsUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {

  operator fun invoke(type: String, app_type: String): Flow<Resource<BaseResponse<SettingsData>>> =
    flow {
      emit(Resource.Loading)
      val result = settingsRepository.settings(type, app_type)
      emit(result)
    }.flowOn(Dispatchers.IO)

  operator fun invoke(
    type: String
  ): Flow<Resource<BaseResponse<List<SettingsData>>>> =
    flow {
      emit(Resource.Loading)
      val result = settingsRepository.social(type, Constants.APP_TYPE_GENERAL)
      emit(result)
    }.flowOn(Dispatchers.IO)

  @Throws(ContactAppValidationException::class)
  operator fun invoke(request: ContactUsRequest): Flow<Resource<BaseResponse<*>>> =
    flow {
      if (request.name.isEmpty()) {
        throw ContactAppValidationException(AuthFieldsValidation.EMPTY_NAME.value.toString())
      }
      if (request.email.isEmpty()) {
        throw ContactAppValidationException(AuthFieldsValidation.EMPTY_EMAIL.value.toString())
      }

      if (!request.email.isValidEmail()) {
        throw ContactAppValidationException(AuthFieldsValidation.INVALID_EMAIL.value.toString())
      }

      if (request.phone.isEmpty()) {
        throw ContactAppValidationException(AuthFieldsValidation.EMPTY_PHONE.value.toString())
      }
      if (request.message.isEmpty()) {
        throw ContactAppValidationException(AuthFieldsValidation.EMPTY_CONTENT.value.toString())
      }

      emit(Resource.Loading)
      val result = settingsRepository.contactApp(request)
      emit(result)
    }.flowOn(Dispatchers.IO)

}