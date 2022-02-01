package app.grand.tafwak.data.settings.repository

import app.grand.tafwak.data.settings.data_source.remote.SettingsRemoteDataSource
import app.grand.tafwak.domain.settings.models.ContactUsRequest
import app.grand.tafwak.domain.settings.models.SettingsData
import app.grand.tafwak.domain.settings.repository.SettingsRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val remoteDataSource: SettingsRemoteDataSource) :
  SettingsRepository {
  override suspend fun settings(
    type: String,
    app_type: String
  ): Resource<BaseResponse<SettingsData>> = remoteDataSource.settings(type, app_type)

  override suspend fun social(
    type: String,
    app_type: String
  ): Resource<BaseResponse<List<SettingsData>>> = remoteDataSource.social(type, app_type)

  override suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>> =
    remoteDataSource.contactApp(contactUsRequest)
}