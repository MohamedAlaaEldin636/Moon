package grand.app.moon.domain.settings.use_case

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.qualifiers.ApplicationContext
import es.dmoral.toasty.Toasty
import grand.app.moon.R
import grand.app.moon.domain.base.FieldsValidation
import grand.app.moon.domain.base.ValidationException
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.settings.models.AppInfoResponse
import grand.app.moon.domain.settings.models.ContactAppValidationException
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.repository.SettingsRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.isValidEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class SettingsUseCase @Inject constructor(
  @ApplicationContext val context: Context,
  private val settingsRepository: SettingsRepository
) {

  fun settings(
    type: String,
  ): Flow<Resource<BaseResponse<List<SettingsData>>>> =
    flow {
      emit(Resource.Loading)
      val result = settingsRepository.settings(type)
      emit(result)
    }.flowOn(Dispatchers.IO)

  fun settingsAppInfo(
    type: String,
  ): Flow<Resource<BaseResponse<AppInfoResponse>>> =
    flow {
      val result = settingsRepository.settingsAppInfo(type)
      emit(result)
    }.flowOn(Dispatchers.IO)

  fun onBoard(
    type: String
  ): Flow<Resource<BaseResponse<List<AppTutorial>>>> =
    flow {
      emit(Resource.Loading)
      val result = settingsRepository.onBoard(type)
      emit(result)
    }.flowOn(Dispatchers.IO)

  fun notifications(type: Int,page: Int): Flow<Resource<BaseResponse<NotificationPaginateData>>> =
    flow {
      emit(Resource.Loading)
      val result = settingsRepository.notifications(type,page)
      emit(result)
    }.flowOn(Dispatchers.IO)

  fun delete(type: Int): Flow<Resource<BaseResponse<*>>> =
    flow {
//      emit(Resource.Loading)
      val result = settingsRepository.deleteNotification(type)
      emit(result)
    }.flowOn(Dispatchers.IO)


  @Throws(ContactAppValidationException::class)
  operator fun invoke(request: ContactUsRequest): Flow<Resource<BaseResponse<*>>> =
    flow {
      emit(Resource.Loading)
      val result = settingsRepository.contactApp(request)
      emit(result)
    }.flowOn(Dispatchers.IO)

}