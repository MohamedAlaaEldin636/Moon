package grand.app.moon.data.settings.repository

import grand.app.moon.data.settings.data_source.remote.SettingsRemoteDataSource
import grand.app.moon.domain.intro.entity.AppTutorial
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

  override suspend fun social(
    type: String,
    app_type: String
  ): Resource<BaseResponse<List<SettingsData>>> = remoteDataSource.social(type, app_type)

  override suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>> =
    remoteDataSource.contactApp(contactUsRequest)
}