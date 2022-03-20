package grand.app.moon.domain.settings.use_case

import grand.app.moon.domain.auth.enums.AuthFieldsValidation
import grand.app.moon.domain.settings.models.ContactAppValidationException
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.repository.SettingsRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.isValidEmail
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