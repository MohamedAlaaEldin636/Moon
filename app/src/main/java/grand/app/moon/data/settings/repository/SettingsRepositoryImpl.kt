package grand.app.moon.data.settings.repository

import grand.app.moon.data.settings.data_source.remote.SettingsRemoteDataSource
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.settings.models.AppInfoResponse
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.repository.SettingsRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val remoteDataSource: SettingsRemoteDataSource) :
  SettingsRepository {
  override suspend fun onBoard(
    type: String
  ): Resource<BaseResponse<List<AppTutorial>>> = remoteDataSource.onBoard(type)

  override suspend fun settings(
    type: String
  ): Resource<BaseResponse<List<SettingsData>>> = remoteDataSource.settings(type)

  override suspend fun settingsAppInfo(
    type: String
  ): Resource<BaseResponse<AppInfoResponse>> = remoteDataSource.settingsAppInfo(type)



  override suspend fun notifications(type: Int): Resource<BaseResponse<NotificationPaginateData>> =
    remoteDataSource.notifications(type)

  override suspend fun deleteNotification(type: Int): Resource<BaseResponse<*>> =
    remoteDataSource.deleteNotification(type)

  override suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>> =
    remoteDataSource.contactApp(contactUsRequest)
}